package Merge;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NutlletEnterprise extends Application {
    // 颜色定义
    private static final Color PRIMARY_COLOR = Color.web("#1A94BC");// #1A94BC
    private static final Color SUCCESS_COLOR = Color.web("#63B006");// #63B006
    private static final Color TITLE_COLOR = Color.web("#11659A");  // #11659A
    private static final Color TEXT_COLOR = Color.web("#000000");



    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(createMainContent());
        root.setBottom(createBottomNav(primaryStage));
        root.setRight(createSidebar(primaryStage));  // 添加右侧栏

        // 包裹主布局的滚动容器
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // 设置场景大小为 1366x768
        Scene scene = new Scene(scrollPane, 1366, 768);

        primaryStage.setTitle("Financial Edition");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setBackground(new Background(new BackgroundFill(
                PRIMARY_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER);

        // 左侧标题部分
        HBox leftSection = new HBox();
        leftSection.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("NUTLLET");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        Label edition = new Label("Enterprise Edition");
        edition.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        edition.setTextFill(Color.WHITE);
        edition.setPadding(new Insets(0, 0, 0, 20));

        leftSection.getChildren().addAll(title, edition);

        // 右侧按钮
        Button personalEditionBtn = new Button("Personal Edition");
        personalEditionBtn.setStyle("-fx-background-color: white; -fx-text-fill: " + toHexString(PRIMARY_COLOR) + "; -fx-border-radius: 3;");
        personalEditionBtn.setOnAction(e -> {
            try {
                new Nutllet().start(new Stage());
                ((Stage) personalEditionBtn.getScene().getWindow()).close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // 使用Region作为弹性空间
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(leftSection, spacer, personalEditionBtn);
        return header;
    }

    private SplitPane createMainContent() {
        SplitPane splitPane = new SplitPane();
        // 调整左右面板的比例为 4:6
        splitPane.setDividerPositions(0.4);

        // 左侧面板
        VBox leftPanel = new VBox(20);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setBackground(new Background(new BackgroundFill(
                Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        leftPanel.setMinWidth(350);

        RevenueExpenditureCard revenueExpenditureCard = new RevenueExpenditureCard();
        PersonalCorporateExpenditureCard personalCorporateExpenditureCard = new PersonalCorporateExpenditureCard();

        leftPanel.getChildren().addAll(revenueExpenditureCard, personalCorporateExpenditureCard);

        ScrollPane leftScrollPane = new ScrollPane(leftPanel);
        leftScrollPane.setFitToWidth(true);
        leftScrollPane.setFitToHeight(true);
        leftScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // 右侧面板
        VBox rightPanel = new VBox(15);
        rightPanel.setPadding(new Insets(20, 20, 20, 0));
        rightPanel.setBackground(new Background(new BackgroundFill(
                Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        Label recentTransactions = new Label("Recent Income or Expenditure");
        recentTransactions.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        recentTransactions.setTextFill(PRIMARY_COLOR);
        recentTransactions.setPadding(new Insets(0, 0, 10, 0));

        // 表头样式优化
        HBox headerBox = new HBox(20);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #f5f5f5; -fx-font-weight: bold; -fx-background-radius: 5;");

        Label headerLabel = new Label("The transaction time, product name, receipt/payment type and amount are displayed below");
        headerLabel.setStyle("-fx-text-fill: #000000; ");
        headerLabel.setWrapText(true);
        headerLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));

        headerBox.getChildren().add(headerLabel);

        // 交易记录列表
        ListView<String> transactionList = new ListView<>();
        transactionList.setItems(getTransactionItems());
        transactionList.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
        transactionList.setPrefHeight(450);
        transactionList.setPadding(new Insets(0));

        // 优化列表项样式
        transactionList.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: black; -fx-font-size: 13px; -fx-padding: 8px;");
                    setBackground(new Background(new BackgroundFill(
                            Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        });

        VBox listContainer = new VBox(10);
        listContainer.getChildren().addAll(headerBox, transactionList);
        VBox.setVgrow(transactionList, Priority.ALWAYS);

        // 右侧滚动面板
        ScrollPane rightScrollPane = new ScrollPane(listContainer);
        rightScrollPane.setFitToWidth(true);
        rightScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-background-insets: 0;");
        rightScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rightScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rightScrollPane.setPrefHeight(550);

        rightPanel.getChildren().addAll(recentTransactions, rightScrollPane);
        VBox.setVgrow(rightScrollPane, Priority.ALWAYS);

        splitPane.getItems().addAll(leftScrollPane, rightPanel);
        return splitPane;
    }

    private HBox createBottomNav(Stage primaryStage) {
        HBox navBar = new HBox();
        navBar.setSpacing(0);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPrefHeight(80);
        navBar.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1 0 0 0;");

        Button homeBtn = createNavButtonWithEmoji("Home", "🏠"); // 🏠
        Button discoverBtn = createNavButtonWithEmoji("Discover", "🔍"); // 🔍
        Button settingsBtn = createNavButtonWithEmoji("Settings", "⚙"); // ⚙

        homeBtn.setOnAction(e -> {
            try {
                new Nutllet().start(new Stage()); // 导航到 Nutllet.java
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        discoverBtn.setOnAction(e -> {
            try {
                new Discover().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        settingsBtn.setOnAction(e -> {
            try {
                new Settings().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        navBar.getChildren().addAll(homeBtn, discoverBtn, settingsBtn);
        return navBar;
    }

    private VBox createSidebar(Stage primaryStage) {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setBackground(new Background(new BackgroundFill(
                Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        // 获取屏幕宽度的 1/3
        double screenWidth = 1366; // 假设屏幕宽度为 1366px
        double columnWidth = screenWidth / 3.0;

        // Tax Forecasting Module
        Label title1 = new Label("Tax Forecasting");
        title1.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title1.setTextFill(TITLE_COLOR);

        Label text1 = new Label("Based on a company's financial data and historical tax records to forecast its future potential tax liabilities.");
        text1.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        text1.setTextFill(Color.BLACK);
        text1.setWrapText(true);

        VBox card1 = new VBox(10, title1, text1);
        card1.setPadding(new Insets(20));
        card1.setPrefWidth(columnWidth);
        card1.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        card1.setStyle("-fx-box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);");

        // Financial Recommendations Module
        Label title2 = new Label("Financial Recommendations for Enterprises");
        title2.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title2.setTextFill(TITLE_COLOR);

        Label text2 = new Label("Based on the company's income and expenditure, the fee for application documents accounts for 40% (2000/5000) of the total expenditure. "
                + "It is recommended to optimize the application process and reduce unnecessary costs for application materials. "
                + "At the same time, tax expenditures account for 6.6% (330/5000) of the total expenditure. "
                + "It is suggested to plan taxes reasonably and make full use of tax preferential policies.");
        text2.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        text2.setTextFill(Color.BLACK);
        text2.setWrapText(true);

        VBox card2 = new VBox(10, title2, text2);
        card2.setPadding(new Insets(20));
        card2.setPrefWidth(columnWidth);
        card2.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        card2.setStyle("-fx-box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);");

        // AI Industry Analysis Module
        Label title3 = new Label("Al Industry Analysis");
        title3.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title3.setTextFill(TITLE_COLOR);

        Label text3 = new Label("Al will analyze market-related situations and trends for you and provide reasonable business-related recommendations.");
        text3.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        text3.setTextFill(Color.BLACK);
        text3.setWrapText(true);

        Button askNowButton = new Button("View detailed AI recommendations!");
        stylePrimaryButton(askNowButton); // 使用与"View More Details"按钮相同的样式
        askNowButton.setOnAction(e -> {
            try { new EP_FinancialAnalysis().start(new Stage()); primaryStage.close(); } catch (Exception ex) { ex.printStackTrace(); }
        });

        VBox card3 = new VBox(10, title3, text3, askNowButton);
        card3.setPadding(new Insets(20));
        card3.setPrefWidth(columnWidth);
        card3.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        card3.setStyle("-fx-box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);");

        // Adding all cards to the sidebar
        sidebar.getChildren().addAll(card1, card2, card3);
        return sidebar;
    }

    private void stylePrimaryButton(Button button) {
        button.setStyle("-fx-text-fill: " + toHexString(PRIMARY_COLOR) + "; -fx-background-color: rgba(255, 255, 255, 0.1);"
                + "-fx-padding: 8px 16px; -fx-border-radius: 20px; -fx-border-color: " + toHexString(PRIMARY_COLOR) + ";"
                + "-fx-background-radius: 20px; -fx-cursor: pointer;");
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private javafx.collections.ObservableList<String> getTransactionItems() {
        javafx.collections.ObservableList<String> items = javafx.collections.FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader("EnterpriseDeals.csv"))) {
            String line;
            boolean isTransactionSection = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("----------------------微信支付账单明细列表--------------------")) {
                    isTransactionSection = true;
                    continue;
                }
                if (isTransactionSection && !line.contains("交易时间,交易类型,交易对方,商品,收/支,金额(元),支付方式,当前状态,交易单号,商户单号,备注")) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        String time = parts[0].replace("\"", "");
                        String product = parts[3].replace("\"", "");
                        String type = parts[4].replace("\"", "");
                        String amount = parts[5].replace("\"", "");

                        // 格式化字符串，使用固定宽度确保对齐
                        String item = String.format("%-20s %-30s %-10s %-10s",
                                time, product, type, amount);
                        items.add(item);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    private Button createNavButtonWithEmoji(String label, String emoji) {
        VBox btnContainer = new VBox();
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.setSpacing(2);

        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 16px;");

        Label textLabel = new Label(label);
        textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        btnContainer.getChildren().addAll(emojiLabel, textLabel);

        Button button = new Button();
        button.setPrefWidth(1366 / 3.0);
        button.setPrefHeight(80);
        button.setGraphic(btnContainer);
        button.setStyle(
                "-fx-background-color: white; -fx-border-color: transparent; -fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #f5f5f5; -fx-border-color: transparent;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: white; -fx-border-color: transparent;"
        ));

        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private class RevenueExpenditureCard extends VBox {
        public RevenueExpenditureCard() {
            setSpacing(10);
            setPadding(new Insets(20));
            setBackground(new Background(new BackgroundFill(
                    Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
            setStyle("-fx-box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);");

            Label title = new Label("Enterprise Revenue & Expenditure");
            title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
            title.setStyle("-fx-text-fill: #1A94BC; ");


            // 计算收入和支出总额
            double totalRevenue = 0;
            double totalExpenditure = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader("EnterpriseDeals.csv"))) {
                String line;
                boolean isTransactionSection = false;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("----------------------微信支付账单明细列表--------------------")) {
                        isTransactionSection = true;
                        continue;
                    }
                    if (isTransactionSection && !line.contains("交易时间,交易类型,交易对方,商品,收/支,金额(元),支付方式,当前状态,交易单号,商户单号,备注")) {
                        String[] parts = line.split(",");
                        if (parts.length >= 6) {
                            String type = parts[4].replace("\"", "");
                            String amount = parts[5].replace("\"", "").replace("¥", "");
                            double value = Double.parseDouble(amount);
                            if (type.equals("收入")) {
                                totalRevenue += value;
                            } else if (type.equals("支出")) {
                                totalExpenditure += value;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 计算余额
            double balance = totalRevenue - totalExpenditure;

            // 创建饼图
            PieChart chart = new PieChart();
            PieChart.Data revenueData = new PieChart.Data("Revenue", totalRevenue);
            PieChart.Data expenditureData = new PieChart.Data("Expenditure", totalExpenditure);
            chart.getData().addAll(revenueData, expenditureData);

            // 设置饼图大小
            chart.setPrefSize(300, 300);
            chart.setMaxSize(300, 300);

            // 设置饼图颜色
            revenueData.getNode().setStyle("-fx-pie-color: " + toHexString(SUCCESS_COLOR) + ";");
            expenditureData.getNode().setStyle("-fx-pie-color: " + toHexString(TITLE_COLOR) + ";");

            // 添加总额标签
            Label totalLabel = new Label(String.format("Balance: ¥%.2f", balance));
            totalLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
            totalLabel.setStyle("-fx-text-fill: #000000; ");

            // "View More Details" 按钮导航到 Intelligent_Transaction_Classifier.java
            Button viewMoreDetailsButton = new Button("VIEW MORE DETAILS");
            stylePrimaryButton(viewMoreDetailsButton);
            viewMoreDetailsButton.setOnAction(e -> {
                try {
                    // 获取当前窗口
                    Stage currentStage = (Stage) viewMoreDetailsButton.getScene().getWindow();
                    // 创建并显示新窗口
                    new Intelligent_Transaction_Classifier().start(new Stage());
                    // 关闭当前窗口
                    currentStage.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            getChildren().addAll(title, chart, totalLabel, viewMoreDetailsButton);
        }
    }

    private class PersonalCorporateExpenditureCard extends VBox {
        public PersonalCorporateExpenditureCard() {
            setSpacing(10);
            setPadding(new Insets(20));
            setBackground(new Background(new BackgroundFill(
                    Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
            setStyle("-fx-box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);");

            Label title = new Label("Personal VS Corporate Expenditures");
            title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
            title.setStyle("-fx-text-fill: #1A94BC; ");

            // 计算企业支出总额
            double corporateExpenditure = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader("EnterpriseDeals.csv"))) {
                String line;
                boolean isTransactionSection = false;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("----------------------微信支付账单明细列表--------------------")) {
                        isTransactionSection = true;
                        continue;
                    }
                    if (isTransactionSection && !line.contains("交易时间,交易类型,交易对方,商品,收/支,金额(元),支付方式,当前状态,交易单号,商户单号,备注")) {
                        String[] parts = line.split(",");
                        if (parts.length >= 6) {
                            String type = parts[4].replace("\"", "");
                            String amount = parts[5].replace("\"", "").replace("¥", "");
                            if (type.equals("支出")) {
                                corporateExpenditure += Double.parseDouble(amount);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 计算个人支出总额
            double personalExpenditure = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader("deals.csv"))) {
                String line;
                boolean isTransactionSection = false;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("----------------------微信支付账单明细列表--------------------")) {
                        isTransactionSection = true;
                        continue;
                    }
                    if (isTransactionSection && !line.contains("交易时间,交易类型,交易对方,商品,收/支,金额(元),支付方式,当前状态,交易单号,商户单号,备注")) {
                        String[] parts = line.split(",");
                        if (parts.length >= 6) {
                            String type = parts[4].replace("\"", "");
                            String amount = parts[5].replace("\"", "").replace("¥", "");
                            if (type.equals("支出")) {
                                personalExpenditure += Double.parseDouble(amount);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 计算总支出
            double totalExpenditure = personalExpenditure + corporateExpenditure;

            // 创建饼图
            PieChart chart = new PieChart();
            PieChart.Data personalData = new PieChart.Data("Personal", personalExpenditure);
            PieChart.Data corporateData = new PieChart.Data("Corporate", corporateExpenditure);
            chart.getData().addAll(personalData, corporateData);

            // 设置饼图大小
            chart.setPrefSize(300, 300);
            chart.setMaxSize(300, 300);

            // 设置饼图颜色
            personalData.getNode().setStyle("-fx-pie-color: " + toHexString(SUCCESS_COLOR) + ";");
            corporateData.getNode().setStyle("-fx-pie-color: " + toHexString(TITLE_COLOR) + ";");

            // 添加总额标签
            Label totalLabel = new Label(String.format("Total Expenditures: ¥%.2f", totalExpenditure));
            totalLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
            totalLabel.setStyle("-fx-text-fill: #000000; ");

            getChildren().addAll(title, chart, totalLabel);
        }
    }
}