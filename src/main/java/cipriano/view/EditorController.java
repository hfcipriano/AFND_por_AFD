package cipriano.view;

import cipriano.Principal;
import cipriano.util.AnalisadorSemantico;
import cipriano.util.Interpretador;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;


public class EditorController {

	@FXML
	BorderPane borderMenu;

	@FXML
	TextArea textArea;

	@FXML
	TextArea textAreaResultado;

	@FXML
	Label labelMessage;

	private Principal principal;

	@FXML
	public void fileClose(){
		System.exit(0);
	}

	@FXML
	public void helpAbout(){
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Sobre o programa");
		alert.setHeaderText("AFND_por_AFD v1.0");
		alert.setContentText("Desenvolvido por Henrique Cipriano \n hfcipriano@gmail.com");

		alert.showAndWait();
	}

	@FXML
	public void ajuda(){
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		//TODO: IMPLEMENTAR
		alert.setTitle("TODO");
		alert.showAndWait();
	}

	@FXML
	public void converter(){
		try{
			AnalisadorSemantico.analisa(textArea.getParagraphs());
			labelMessage.setText("Compilado com sucesso!");
			Interpretador.converter(textArea.getParagraphs());
			principal.opentGrafoViewer();
		}catch (RuntimeException e){
			labelMessage.setText(e.getMessage());
		}
	}

	@FXML
	public void mockAFND(){
		String sb = "q0\n" +
					"q3\n" +
					"q0 a q0\n" +
					"q0 a q1\n" +
					"q0 b q0\n" +
					"q1 a q2\n" +
					"q2 a q3";
		textArea.setText(sb);
	}

	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}
}