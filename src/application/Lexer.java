package application;

import java.util.ArrayList;
import java.util.Stack;

public class Lexer {
	
	public enum State {
		TEXT,
		ITALICS,
		BOLD,
	}
	
	public enum StackTypes {
		ITALICS,
		BOLD,
	}
	
	private ArrayList<Token> tokenList = new ArrayList<Token>();
	private State readMode = State.TEXT;
	private Token currentWorkingToken;
	private Stack<StackTypes> stack = new Stack<StackTypes>();
	
	// Lexes a string of markdown and creates an ArrayList of tokens
	public ArrayList<Token> lex(String md) {
		//Scanner input = new Scanner(System.in);
		String[] splitStr = md.split("");
		for (String ch: splitStr) {
			switch (this.readMode) {
				case TEXT:
					this.handleText(ch);
					break;
				case ITALICS:
					this.handleItalics(ch);
					break;
				case BOLD:
					this.handleBold(ch);
					break;
				default:
					break;
			}   
        }
		if (this.currentWorkingToken != null) {
			this.setWorkingToken();
		}
		return this.tokenList;
	}
	
	private void addToken(Token t) {
		this.tokenList.add(t);
	}
	
	private void setWorkingToken() {
		if (this.currentWorkingToken != null) {
			this.tokenList.add(this.currentWorkingToken);
			this.currentWorkingToken = null;
		}
	}
	
	private void handleText(String ch) {
		if (ch.equals("*")) {
			if (!this.stack.isEmpty() && this.stack.peek() == StackTypes.ITALICS) {
				this.setWorkingToken();
				this.stack.pop();
				this.currentWorkingToken = new CloseItalicsToken();
				this.readMode = State.TEXT;
				
			} else {
				this.setWorkingToken();
	        	this.currentWorkingToken = new ItalicsToken();
	        	this.readMode = State.ITALICS;
			}
		} else if (ch.equals("#")) {
			this.setWorkingToken();
        	this.currentWorkingToken = new H1Token();
		} else if (ch.equals("\n")) {
			this.setWorkingToken();
			this.addToken(new LineBreakToken());
        } else {
        	if (this.currentWorkingToken == null || !(this.currentWorkingToken instanceof TextToken)) {
        		this.setWorkingToken();
        		this.currentWorkingToken = new TextToken();
        	} 
        	this.currentWorkingToken.setValue(this.currentWorkingToken.getValue() + ch);
        	
        }
	}
	
	private void handleItalics(String ch) {
		if (ch.equals("*")) {
			if (!this.stack.isEmpty() && this.stack.peek() == StackTypes.BOLD) {
				this.stack.pop();
				this.currentWorkingToken = new CloseBoldToken();
				this.readMode = State.TEXT;
			} else {
	        	this.currentWorkingToken = new BoldToken();
	        	this.readMode = State.BOLD;
			}
        } else {
        	this.stack.push(StackTypes.ITALICS);
        	this.setWorkingToken();
        	this.readMode = State.TEXT;
        	this.handleText(ch);
        }
	}
	
	private void handleBold(String ch) {
        this.stack.push(StackTypes.BOLD);
        this.setWorkingToken();
        this.readMode = State.TEXT;
        this.handleText(ch);
	}
	
	public String displayTokens(ArrayList<Token> tokenList) {
		String outStr = "";
		for (Token t : tokenList) {
			outStr += t.toString();
		}
		return outStr;
	}
	 
}
