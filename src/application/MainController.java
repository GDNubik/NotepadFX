package application;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.mozilla.universalchardet.UniversalDetector;

public class MainController {

    @FXML
    private BorderPane root;

    @FXML
    private TextArea textArea;

    @FXML
    private Label positionLabel;

    @FXML
    private Label charsetLabel;

    @FXML
    private MenuItem undo;

    @FXML
    private MenuItem redo;

    @FXML
    private Menu exit;

    @FXML
    private MenuItem saveText;

    private File pathToFile;

    private static final Map<Character, Character> RUS_TO_ENG = new HashMap<>();
    private static final Map<Character, Character> ENG_TO_RUS = new HashMap<>();

    static {

        RUS_TO_ENG.put('й', 'q'); RUS_TO_ENG.put('ц', 'w'); RUS_TO_ENG.put('у', 'e');
        RUS_TO_ENG.put('к', 'r'); RUS_TO_ENG.put('е', 't'); RUS_TO_ENG.put('н', 'y');
        RUS_TO_ENG.put('г', 'u'); RUS_TO_ENG.put('ш', 'i'); RUS_TO_ENG.put('щ', 'o');
        RUS_TO_ENG.put('з', 'p'); RUS_TO_ENG.put('х', '['); RUS_TO_ENG.put('Х', '{');
        RUS_TO_ENG.put('ъ', ']'); RUS_TO_ENG.put('Ъ', '}'); RUS_TO_ENG.put('ф', 'a');
        RUS_TO_ENG.put('ы', 's'); RUS_TO_ENG.put('в', 'd'); RUS_TO_ENG.put('а', 'f');
        RUS_TO_ENG.put('п', 'g'); RUS_TO_ENG.put('р', 'h'); RUS_TO_ENG.put('о', 'j');
        RUS_TO_ENG.put('л', 'k'); RUS_TO_ENG.put('д', 'l'); RUS_TO_ENG.put('ж', ';');
        RUS_TO_ENG.put('Ж', ':'); RUS_TO_ENG.put('э', '\''); RUS_TO_ENG.put('Э', '"');
        RUS_TO_ENG.put('/', '|'); RUS_TO_ENG.put('я', 'z'); RUS_TO_ENG.put('ч', 'x');
        RUS_TO_ENG.put('с', 'c'); RUS_TO_ENG.put('м', 'v'); RUS_TO_ENG.put('и', 'b');
        RUS_TO_ENG.put('т', 'n'); RUS_TO_ENG.put('ь', 'm'); RUS_TO_ENG.put('б', ',');
        RUS_TO_ENG.put('Б', '<'); RUS_TO_ENG.put('ю', '.'); RUS_TO_ENG.put('Ю', '>');
        RUS_TO_ENG.put(',', '?'); RUS_TO_ENG.put('.', '/'); RUS_TO_ENG.put('ё', '`');
        RUS_TO_ENG.put('Ё', '~'); RUS_TO_ENG.put('"', '@'); RUS_TO_ENG.put('№', '#');
        RUS_TO_ENG.put(';', '$'); RUS_TO_ENG.put(':', '^'); RUS_TO_ENG.put('?', '&');


        ENG_TO_RUS.put('q', 'й'); ENG_TO_RUS.put('w', 'ц'); ENG_TO_RUS.put('e', 'у');
        ENG_TO_RUS.put('r', 'к'); ENG_TO_RUS.put('t', 'е'); ENG_TO_RUS.put('y', 'н');
        ENG_TO_RUS.put('u', 'г'); ENG_TO_RUS.put('i', 'ш'); ENG_TO_RUS.put('o', 'щ');
        ENG_TO_RUS.put('p', 'з'); ENG_TO_RUS.put('[', 'х'); ENG_TO_RUS.put('{', 'Х');
        ENG_TO_RUS.put(']', 'ъ'); ENG_TO_RUS.put('}', 'Ъ'); ENG_TO_RUS.put('a', 'ф');
        ENG_TO_RUS.put('s', 'ы'); ENG_TO_RUS.put('d', 'в');  ENG_TO_RUS.put('f', 'а');
        ENG_TO_RUS.put('g', 'п'); ENG_TO_RUS.put('h', 'р'); ENG_TO_RUS.put('j', 'о');
        ENG_TO_RUS.put('k', 'л'); ENG_TO_RUS.put('l', 'д'); ENG_TO_RUS.put(';', 'ж');
        ENG_TO_RUS.put(':', 'Ж'); ENG_TO_RUS.put('\'', 'э'); ENG_TO_RUS.put('"', 'Э');
        ENG_TO_RUS.put('|', '/'); ENG_TO_RUS.put('z', 'я'); ENG_TO_RUS.put('x', 'ч');
        ENG_TO_RUS.put('c', 'с'); ENG_TO_RUS.put('v', 'м'); ENG_TO_RUS.put('b', 'и');
        ENG_TO_RUS.put('n', 'т'); ENG_TO_RUS.put('m', 'ь'); ENG_TO_RUS.put(',', 'б');
        ENG_TO_RUS.put('<', 'Б'); ENG_TO_RUS.put('.', 'ю'); ENG_TO_RUS.put('>', 'Ю');
        ENG_TO_RUS.put('?', ','); ENG_TO_RUS.put('/', '.'); ENG_TO_RUS.put('`', 'ё');
        ENG_TO_RUS.put('~', 'Ё'); ENG_TO_RUS.put('@', '"'); ENG_TO_RUS.put('#', '№');
        ENG_TO_RUS.put('$', ';'); ENG_TO_RUS.put('^', ':'); ENG_TO_RUS.put('&', '?');
    }

    @FXML
    private void initialize() {
        textArea.caretPositionProperty().addListener((obs, oldPos, newPos) -> updateCursorPosition(newPos.intValue()));

        undo.disableProperty().bind(textArea.undoableProperty().not());
        redo.disableProperty().bind(textArea.redoableProperty().not());

        saveText.setDisable(true);

        ContextMenu rightButtonMenu = new ContextMenu();

        MenuItem cut = new MenuItem("Вырезать");
        cut.setOnAction(e -> textArea.cut());

        MenuItem copy = new MenuItem("Копировать");
        copy.setOnAction(e -> textArea.copy());

        MenuItem paste = new MenuItem("Вставить");
        paste.setOnAction(e -> textArea.paste());

        MenuItem delete = new MenuItem("Удалить");
        delete.setOnAction(e -> textArea.deleteText(textArea.getSelection()));

        MenuItem selectAll = new MenuItem("Выделить всё");
        selectAll.setOnAction(e -> textArea.selectAll());

        MenuItem changeLayout = new MenuItem("Сменить раскладку");
        changeLayout.setOnAction(e -> changeLayout());

        SeparatorMenuItem sep = new SeparatorMenuItem();

        rightButtonMenu.getItems().addAll(cut, copy, paste, delete, selectAll, changeLayout);

        rightButtonMenu.getItems().add(5, sep);

        textArea.setContextMenu(null);

        textArea.setOnContextMenuRequested(event -> {

            boolean hasSelection = textArea.getSelection().getLength() > 0;
            boolean hasText = textArea.getLength() > 0;
            boolean canEdit = textArea.isEditable();
            boolean allSelected = textArea.getSelection().getLength() == textArea.getLength();

            cut.setDisable(!hasSelection || !canEdit);
            copy.setDisable(!hasSelection);
            paste.setDisable(!canEdit);
            delete.setDisable(!hasSelection || !canEdit);
            selectAll.setDisable(!hasText || allSelected);
            changeLayout.setDisable(!hasSelection || !canEdit);

            rightButtonMenu.show(textArea, event.getScreenX(), event.getScreenY());
            event.consume();
        });

        textArea.setOnMousePressed(event -> {
            if (rightButtonMenu.isShowing())
                rightButtonMenu.hide();
        });

        textArea.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, e -> {
                    if (rightButtonMenu.isShowing())
                        rightButtonMenu.hide();
                });
            }
        });

        undo.setOnAction(e -> textArea.undo());
        redo.setOnAction(e -> textArea.redo());

        Platform.runLater(()-> {
            Stage stage = (Stage) textArea.getScene().getWindow();
            stage.setOnCloseRequest(this::exitNotepad2);
        });
    }

    private void updateCursorPosition(int caret) {
        ObservableList<CharSequence> paragraphs = textArea.getParagraphs();
        
        int line = 1;
        int charsCounted = 0;
        int column = 1;

        for (CharSequence paragraph : paragraphs) {
            int pLength = paragraph.length();
            
            if (caret <= charsCounted + pLength) {
                column = caret - charsCounted + 1;
                break;
            }
            
            charsCounted += pLength + 1;
            line++;
        }

        positionLabel.setText("Строка " + line + ", столбец " + column);
    }
    
    @FXML
    private void saveText(ActionEvent event) {
        try {
            if (pathToFile != null) {
                savingTextFile(pathToFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.toString());

            alert.showAndWait();
        }
    }

    @FXML
    private void saveTextAs(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();

            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
            );

            if (pathToFile != null && pathToFile.getParentFile().exists()) {
                fileChooser.setInitialDirectory(pathToFile.getParentFile());
                fileChooser.setInitialFileName(pathToFile.getName());
            }
            
            File file = fileChooser.showSaveDialog(textArea.getScene().getWindow());
            
            if (file != null) {
                savingTextFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.toString());

            alert.showAndWait();
        }
    }

    private void savingTextFile(File file) throws Exception { 
        String content = textArea.getText();

        Files.write(
            Paths.get(file.getAbsolutePath()),
            content.getBytes(StandardCharsets.UTF_8)
        );

        Stage stage = (Stage) textArea.getScene().getWindow();
        stage.setTitle(file.getName() + " - NotepadFX");

        pathToFile = file;

        saveText.setDisable(false);
    }

    private String detectCharset(File file) {
        try {
            byte[] buf = new byte[4096];
            UniversalDetector detector = new UniversalDetector(null);

            try (var fis = new java.io.FileInputStream(file)) {
                int n;
                while ((n = fis.read(buf)) > 0 && !detector.isDone()) {
                    detector.handleData(buf, 0, n);
                }
            }

            detector.dataEnd();
            String charset = detector.getDetectedCharset();

            if (charset == null) charset = "UTF-8";
            return charset;

        } catch (Exception e) {
            return "UTF-8";
        }
    }

    @FXML
    private void openTextFile(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();

            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
            );
            
            File file = fileChooser.showOpenDialog(textArea.getScene().getWindow());
            
            if (file != null) {
                if (file.length() <= 1048576){
                    openingTextFile(file);
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Файл слишком большой");
                    alert.setHeaderText("Файл слишком большой,\nвы точно хотите его открыть?");
                    alert.setContentText("Если вы откроете этот файл, программа может зависнуть");

                    ButtonType YesButtonType = new ButtonType("Да");
                    ButtonType NoButtonType = new ButtonType("Нет");
                    alert.getButtonTypes().setAll(YesButtonType, NoButtonType);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == YesButtonType){
                        openingTextFile(file);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.toString());

            alert.showAndWait();
        }
    }

    private void openingTextFile(File file) throws Exception {
        textArea.clear();

        String charset = detectCharset(file);

        String content = new String(
            Files.readAllBytes(Paths.get(file.getAbsolutePath())),
            Charset.forName(charset)
        );
                    
        textArea.setText(content);

        switch (charset) {
            case "WINDOWS-1251":
                charsetLabel.setText("Windows-1251");
                break;
            default:
                charsetLabel.setText(charset);
        }
                    
        textArea.positionCaret(0);

        Stage stage = (Stage) textArea.getScene().getWindow();
        stage.setTitle(file.getName() + " - NotepadFX");

        pathToFile = file;

        saveText.setDisable(false);
    }

    @FXML
    private void exitNotepad1() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Сохранить файл?");
        alert.setHeaderText("Сохранить файл перед выходом?");

        ButtonType YesButtonType = new ButtonType("Да");
        ButtonType NoButtonType = new ButtonType("Нет");
        ButtonType CancelButtonType = new ButtonType("Отмена");
        alert.getButtonTypes().setAll(YesButtonType, NoButtonType, CancelButtonType);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == YesButtonType){
            saveTextAs(null);
            Platform.exit();
        } else if (result.get() == NoButtonType){
            Platform.exit();
        }
    }

    private void exitNotepad2(WindowEvent event) {
        event.consume();

        exitNotepad1();
    }

    @FXML
    private void hideExit() {
        exit.hide();
    }

    @FXML
    private void openInfoWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("resources/fxml/Info.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            
            Stage infoStage = new Stage();

            infoStage.setTitle("О приложении");
            infoStage.getIcons().add(new Image(getClass().getClassLoader().getResource("resources/images/about_icon.png").toExternalForm()));
            infoStage.initModality(Modality.WINDOW_MODAL);
            infoStage.initOwner(textArea.getScene().getWindow());
            infoStage.setScene(scene);
            infoStage.setResizable(false);
            
            infoStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.toString());

            alert.showAndWait();
        }
    }

    private void changeLayout() {
        String selectedText = textArea.getSelectedText();
        StringBuilder convertedText = new StringBuilder();

        for (char ch : selectedText.toCharArray()) {

            if (ENG_TO_RUS.containsKey(ch)) {
                convertedText.append(ENG_TO_RUS.get(ch));
            } 
            else if (RUS_TO_ENG.containsKey(ch)) {
                convertedText.append(RUS_TO_ENG.get(ch));
            } 
            else {
                convertedText.append(ch);
            }
        }

        replaceSelectedText(convertedText.toString());
    }

    private void replaceSelectedText(String newText) {
        int start = textArea.getSelection().getStart();
        
        textArea.replaceSelection(newText.toString());
        
        textArea.selectRange(start, start + newText.length());
    }
}