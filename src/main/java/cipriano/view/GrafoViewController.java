package cipriano.view;

import cipriano.Principal;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by henrique on 27/08/16.
 */
public class GrafoViewController {
    @FXML
    ImageView grafoView;

    private Principal principal;

    public void inserirImagem(File arquivo) throws FileNotFoundException {
        grafoView.setImage(new Image(new FileInputStream(arquivo)));
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }


}
