package cipriano.view;

import cipriano.Principal;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by henrique on 27/08/16.
 */
public class GrafoViewController {
    @FXML
    ImageView grafoView;

    private Principal principal;

    public void inserirImagem(){
        grafoView.setImage(new Image(principal.getClass().getResourceAsStream("resources/grafo.png")));
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }


}
