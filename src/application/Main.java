/*
 * Created by Enda Hallahan.
 * 
 * This program allows the user to enter a string of simple markdown,
 * then tokenizes, parses, and finally constructs valid HTML from it.
 * 
 * Currently this program only handles italics(*), bold(*), paragraphs, and H1 (#).
 * However, it could be easily expanded to handle a full markdown spec.
 */

package application;
	
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class Main extends Application {
	
	private TextArea taMarkdownIn = new TextArea();
	private TextArea taTokensOut = new TextArea();
	private TextArea taAstOut = new TextArea();
	private TextArea taHtmlOut = new TextArea();
	private Button btConvert = new Button("Convert");
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			primaryStage.setTitle("Md to HTML");
			
			GridPane gridPane = new GridPane();
			
			int taHeight = 500;
			int taWidth = 400;
			taMarkdownIn.setPrefWidth(taWidth);
			taMarkdownIn.setPrefHeight(taHeight);
			taTokensOut.setPrefWidth(taWidth);
			taTokensOut.setPrefHeight(taHeight);
			taAstOut.setPrefWidth(taWidth);
			taAstOut.setPrefHeight(taHeight);
			taHtmlOut.setPrefWidth(taWidth);
			taHtmlOut.setPrefHeight(taHeight);
			
			taMarkdownIn.setWrapText(true);
			taTokensOut.setWrapText(true);
			taAstOut.setWrapText(true);
			taHtmlOut.setWrapText(true);
			
			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER);
			hbox.getChildren().add(btConvert);
			
			gridPane.setHgap(5);
			gridPane.setVgap(5);
			
			gridPane.add(new Label("Enter Markdown: "), 0, 0);
			gridPane.add(taMarkdownIn, 0, 1);
			gridPane.add(hbox, 0, 2, 4, 1);
			gridPane.add(new Label("Token String: "), 1, 0);
			gridPane.add(taTokensOut, 1, 1);
			gridPane.add(new Label("AST: "), 2, 0);
			gridPane.add(taAstOut, 2, 1);
			gridPane.add(new Label("HTML: "), 3, 0);
			gridPane.add(taHtmlOut, 3, 1);
			
			btConvert.setOnAction(e -> convert());
			
			Scene scene = new Scene(gridPane,1000,600);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void convert() {
		// Lexes md and displays a list of tokens
		Lexer mdLexer = new Lexer();
		ArrayList<Token> tokenList = mdLexer.lex(taMarkdownIn.getText());
		taTokensOut.setText(mdLexer.displayTokens(tokenList));
		
		// Parses the string of tokens into an ast and displays it
		Parser mdParser = new Parser();
		Group parsedTree = mdParser.parse(tokenList);
		taAstOut.setText(mdParser.displayTree(parsedTree, 0));
		
		// Constructs HTML from the ast and displays it
		HTMLConstructor htmlCon = new HTMLConstructor();
		taHtmlOut.setText(htmlCon.construct(parsedTree));
	}
}
