package application;

public class HTMLConstructor {
	
	// Travels an abstract syntax tree and generates valid HTML.
	public String construct(Group ast) {
		return parseGroup(ast);
	}
	
	private String parseGroup(Group g) {
		String outStr = "";
		switch (g.getType()) {
			case TEXT:
				outStr += g.getText();
				break;
			case PARAGRAPH:
				outStr += "<p>";
				break;
			case ITALICS:
				outStr += "<em>";
				break;
			case BOLD:
				outStr += "<strong>";
				break;
			case H1:
				outStr += "<h1>";
				break;
			default:
				break;
		}
		
		for (Group child: g.getChildren()) {
			outStr += parseGroup(child);
		}
		
		switch (g.getType()) {
			case PARAGRAPH:
				outStr += "</p>";
				break;
			case ITALICS:
				outStr += "</em>";
				break;
			case BOLD:
				outStr += "</strong>";
				break;
			case H1:
				outStr += "</h1>";
				break;
			default:
				break;
				
		}
		
		return outStr;
	}
	
}
