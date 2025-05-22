package Merge;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Settings extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        VBox mainLayout = new VBox();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setSpacing(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setBackground(new Background(new BackgroundFill(
                Color.web("#E6F0FF", 0.3), new CornerRadii(0), Insets.EMPTY)));

        Text title = new Text("Settings");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setFill(Color.BLACK);
        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(750);
        searchField.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 4; -fx-padding: 10 15; -fx-min-height: 40px; -fx-font-size: 16px;");
        searchField.setAlignment(Pos.CENTER);

        Label searchIcon = new Label("🔍");
        searchIcon.setStyle("-fx-font-size: 20px; -fx-text-fill: #7f8c8d;");

        HBox searchBox = new HBox(10, searchIcon, searchField);
        searchBox.setPrefWidth(800);
        searchBox.setAlignment(Pos.CENTER);

        VBox itemsContainer = new VBox();
        itemsContainer.setSpacing(15);
        itemsContainer.setAlignment(Pos.CENTER);
        itemsContainer.setPadding(new Insets(10));

        Label noResultsLabel = new Label("Relevant functions are building forward...");
        noResultsLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        noResultsLabel.setTextFill(Color.GRAY);
        noResultsLabel.setVisible(false);

        ScrollPane scrollPane = new ScrollPane(itemsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(600);

        String[] pastelColors = {
                "#FFB6C1B3", "#FFDAB9B3", "#FFFACDB3", "#E0FFFFB3", "#D8BFD8B3", "#C6E2FFB3", "#E6E6FAB3"
        };

        String[] titles = {
                "Enterprise Edition",
                "App Feedback",
                "AI Advice",
                "Change Password",
                "Sign Up with New Account",
                "Log out"
        };

        String[] descriptions = {
                "Click to start with Enterprise Edition.",
                "In case you wish to give us some suggestions.",
                "Set your preferred AI.",
                "Change your password.",
                "Change your account or to have a new account.",
                "Click to logout"
        };

        for (int i = 0; i < titles.length; i++) {
            Button btn = createSettingButton(primaryStage, titles[i], descriptions[i], pastelColors[i % pastelColors.length]);
            addHoverAnimation(btn);
            itemsContainer.getChildren().add(btn);
        }

        mainLayout.getChildren().addAll(titleBox, searchBox, scrollPane, noResultsLabel);
        root.setCenter(mainLayout);

        // Bottom Navigation Bar with highlight
        HBox bottomNavigationBar = new HBox();
        bottomNavigationBar.setSpacing(0);
        bottomNavigationBar.setAlignment(Pos.CENTER);
        bottomNavigationBar.setPrefHeight(80);
        bottomNavigationBar.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1 0 0 0;");

        String currentPage = "Settings";

        Button homeButton = createNavButtonWithHighlight("Home", "🏠", currentPage.equals("Home"));
        Button discoverButton = createNavButtonWithHighlight("Discover", "🔍", currentPage.equals("Discover"));
        Button settingsButton = createNavButtonWithHighlight("Settings", "⚙", currentPage.equals("Settings"));

        homeButton.setOnAction(e -> {
            try { new Nutllet().start(new Stage()); primaryStage.close(); } catch (Exception ex) { ex.printStackTrace(); }
        });
        discoverButton.setOnAction(e -> {
            try { new Discover().start(new Stage()); primaryStage.close(); } catch (Exception ex) { ex.printStackTrace(); }
        });
        settingsButton.setOnAction(e -> {
            // 当前页不跳转
        });

        bottomNavigationBar.getChildren().addAll(homeButton, discoverButton, settingsButton);
        root.setBottom(bottomNavigationBar);

        // 搜索过滤逻辑（含暂无匹配内容提示）
        FilteredList<Button> filteredButtons = new FilteredList<>(
                FXCollections.observableArrayList(
                        itemsContainer.getChildren().filtered(n -> n instanceof Button).toArray(Button[]::new)
                ),
                p -> true
        );

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal.toLowerCase();
            filteredButtons.setPredicate(btn -> {
                if (btn.getGraphic() instanceof HBox graphic) {
                    for (Node n : graphic.getChildren()) {
                        if (n instanceof VBox textContent) {
                            for (Node labelNode : textContent.getChildren()) {
                                if (labelNode instanceof Text textNode && textNode.getFont().getStyle().contains("Bold")) {
                                    return textNode.getText().toLowerCase().contains(lower);
                                }
                            }
                        }
                    }
                }
                return false;
            });
            itemsContainer.getChildren().setAll(filteredButtons);
            noResultsLabel.setVisible(filteredButtons.isEmpty());
        });

        // Animation
        FadeTransition fade = new FadeTransition(Duration.seconds(1), mainLayout);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.seconds(1), mainLayout);
        slide.setFromY(50);
        slide.setToY(0);

        fade.play();
        slide.play();

        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setTitle("Settings");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createNavButtonWithHighlight(String labelText, String emojiSymbol, boolean isActive) {
        VBox buttonContent = new VBox();
        buttonContent.setAlignment(Pos.CENTER);
        buttonContent.setSpacing(2);

        Label emojiLabel = new Label(emojiSymbol);
        emojiLabel.setStyle("-fx-font-size: 16px;" + (isActive ? " -fx-text-fill: #855FAF;" : ""));

        Label textLabel = new Label(labelText);
        textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + (isActive ? "#855FAF; -fx-font-weight: bold;" : "#7f8c8d;"));

        buttonContent.getChildren().addAll(emojiLabel, textLabel);

        Button navigationButton = new Button();
        navigationButton.setPrefWidth(456);
        navigationButton.setPrefHeight(80);
        navigationButton.setGraphic(buttonContent);
        navigationButton.setStyle("-fx-background-color: " + (isActive ? "#F0F0F0;" : "white;") + " -fx-border-color: transparent;");

        return navigationButton;
    }

    private void addHoverAnimation(Button button) {
        button.setOnMouseEntered(e -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
            scaleUp.setToX(1.05);
            scaleUp.setToY(1.05);
            scaleUp.play();
        });
        button.setOnMouseExited(e -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
            scaleDown.setToX(1);
            scaleDown.setToY(1);
            scaleDown.play();
        });
    }

    private String getEmojiForTitle(String title) {
        return switch (title) {
            case "Enterprise Edition" -> "\uD83C\uDFE2";
            case "App Feedback" -> "\uD83D\uDCAC";
            case "AI Advice" -> "\uD83E\uDD16";
            case "Change Password" -> "\uD83D\uDD12";
            case "Sign Up with New Account" -> "\uD83C\uDD95";
            case "Log out" -> "\uD83D\uDEAA";
            default -> "⚙️";
        };
    }

    private Button createSettingButton(Stage primaryStage, String title, String description, String bgColor) {
        Button button = new Button();
        button.setMaxWidth(800);
        button.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12; -fx-border-radius: 12; -fx-padding: 15 20; -fx-font-size: 16px; -fx-text-alignment: left;");

        Label emojiLabel = new Label(getEmojiForTitle(title));
        emojiLabel.setFont(Font.font("Arial", 24));
        emojiLabel.setPadding(new Insets(0, 5, 0, 0));

        VBox textContent = new VBox();
        textContent.setSpacing(5);
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleText.setFill(Color.web("#2c3e50"));

        Text descriptionText = new Text(description);
        descriptionText.setFont(Font.font("Arial", 14));
        descriptionText.setFill(Color.web("#7f8c8d"));

        textContent.getChildren().addAll(titleText, descriptionText);

        HBox graphicBox = new HBox(10, emojiLabel, textContent);
        graphicBox.setAlignment(Pos.CENTER_LEFT);

        button.setGraphic(graphicBox);
        button.setOnAction(e -> openNewPage(primaryStage, title));

        return button;
    }

    private void showDefaultWelcomePage(Stage primaryStage, String pageTitle) {
        VBox newPageLayout = new VBox();
        newPageLayout.setAlignment(Pos.CENTER);
        newPageLayout.setSpacing(20);
        newPageLayout.setStyle("-fx-background-color: #FFFBE6;");
        Text label = new Text("Welcome to: " + pageTitle);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        label.setFill(Color.web("#855FAF"));

        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: #855FAF; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-background-radius: 6;");
        backBtn.setOnAction(e -> start(primaryStage));

        newPageLayout.getChildren().addAll(label, backBtn);
        Scene newScene = new Scene(newPageLayout, 1366, 768);
        primaryStage.setScene(newScene);
    }
    private void openNewPage(Stage primaryStage, String pageTitle) {
        try {
            switch (pageTitle) {
                case "Enterprise Edition" -> new NutlletEnterprise().start(new Stage());
                case "App Feedback" -> new Mailbox().start(new Stage());
                case "AI Advice" -> new FinancialAnalysis().start(new Stage());
                case "Change Password" -> new LoginPassword().start(new Stage());
                case "Sign Up with New Account" -> new LoginSignUp().start(new Stage());
                case "Log out" -> new Login().start(new Stage());
                default -> showDefaultWelcomePage(primaryStage, pageTitle);
            }
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
