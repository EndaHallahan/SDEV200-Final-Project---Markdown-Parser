package application;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {
	private Group ast = new Group(Group.GroupType.ROOT);
	private Group workingGroup = ast;
	
	// Parses an ArrayList of tokens and produces an abstract syntax tree
	public Group parse(ArrayList<Token> tokenList) {
		for (Token token: tokenList) {
			if (token instanceof TextToken) {
				CheckInlineBlock();
				Group newTextGroup = new Group(Group.GroupType.TEXT);
				newTextGroup.setText(token.getValue());
				this.workingGroup.addChild(newTextGroup);
			} else if (token instanceof ItalicsToken) {
				CheckInlineBlock();
				this.addGroupAndMoveTo(new Group(Group.GroupType.ITALICS));
			} else if (token instanceof CloseItalicsToken) {
				this.closeWorkingGroup();
			} else if (token instanceof BoldToken) {
				CheckInlineBlock();
				this.addGroupAndMoveTo(new Group(Group.GroupType.BOLD));
			} else if (token instanceof CloseBoldToken) {
				this.closeWorkingGroup();
			} else if (token instanceof H1Token) {
				this.addGroupAndMoveTo(new Group(Group.GroupType.H1));
			}  else if (token instanceof LineBreakToken) {
				Stack<Group.GroupType> stack = new Stack<Group.GroupType>();
				while (this.workingGroup.getType() != Group.GroupType.PARAGRAPH
						&& this.workingGroup.getType() != Group.GroupType.ROOT) {
					stack.push(this.workingGroup.getType());
					this.closeWorkingGroup();
				}
				this.closeWorkingGroup();
				this.addGroupAndMoveTo(new Group(Group.GroupType.PARAGRAPH));
				// Preserves open groups across paragraphs safely
				while (!stack.isEmpty()) {
					Group.GroupType g = stack.pop();
					if (g != Group.GroupType.H1) { // Do not reopen Heading groups
						this.addGroupAndMoveTo(new Group(g));
					}
				}
			} else {
				
			}
		}
		return ast;
	}
	
	private void addGroupAndMoveTo(Group g) {
		this.workingGroup.addChild(g);
		this.workingGroup = g;
	}
	
	private void closeWorkingGroup() {
		if (this.workingGroup.getType() != Group.GroupType.ROOT) {
			this.workingGroup = this.workingGroup.getParent();
		}
	}
	
	private void CheckInlineBlock() {
		if (this.workingGroup.getType() == Group.GroupType.ROOT) {
			this.addGroupAndMoveTo(new Group(Group.GroupType.PARAGRAPH));
		}
	}
	
	public String displayTree(Group inGroup, int depth) {
		String outStr = "";
		for (int i = 0; i < depth; i++) {
			outStr += "\t";
		}
		outStr += inGroup + "\n";
		for (Group g: inGroup.getChildren()) {
			outStr += this.displayTree(g, depth + 1);
		}
		return outStr;
	}
}
