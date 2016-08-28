package cipriano;

import cipriano.view.EditorController;
import cipriano.view.GrafoViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
* Classe que gerencia o módulo do Java Fx.
**/
public class Principal extends Application {

    private Stage primaryStage;
    private BorderPane painel;
    private EditorController editorController;
    private GrafoViewController grafoViewController;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AFND_por_AFD");
        this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/html_app_32.png")));
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
            loader.setLocation(getClass().getResource("/view/Editor.fxml"));
            painel = (BorderPane) loader.load();

            Scene scene = new Scene(painel);
            primaryStage.setScene(scene);

            editorController = loader.getController();
            editorController.setPrincipal(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Instancia o editor
     */
    public void opentGrafoViewer() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/GrafoView.fxml"));
            AnchorPane view = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Grafo finito determinístico");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(view);
            dialogStage.setScene(scene);

            // Define a pessoa no controller.
            GrafoViewController controller = loader.getController();
            controller.setPrincipal(this);
            controller.inserirImagem();

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
