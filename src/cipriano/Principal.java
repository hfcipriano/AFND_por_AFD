package cipriano;

import java.io.IOException;
import java.util.List;

import cipriano.view.EditorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
* Classe que gerencia o módulo do Java Fx.
**/
public class Principal extends Application {

    private Stage primaryStage;
    private EditorController editorController;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AFND_por_AFD");
        this.primaryStage.getIcons().add(new Image("/cipriano/resources/images/html_app_32.png"));
        initEditor();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Instancia o editor
     */
    private void initEditor() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(cipriano.Principal.class.getResource("view/Editor.fxml"));
            BorderPane painel = (BorderPane) loader.load();

            Scene scene = new Scene(painel);
            primaryStage.setScene(scene);

            editorController = loader.getController();
            editorController.setPrincipal(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
