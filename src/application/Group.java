package application;

import java.util.ArrayList;

public class Group {	
	public enum GroupType {
		ROOT,
		PARAGRAPH,
		TEXT, 
		ITALICS,
		BOLD,
		H1,
	}
	
	private String text;
	private GroupType type = GroupType.TEXT;
	private Group parent;
	private ArrayList<Group> children = new ArrayList<Group>();
	
	public Group(GroupType type) {
		this.type = type;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public GroupType getType() {
		return this.type;
	}
	
	public void setType(GroupType type) {
		this.type = type;
	}
	
	public ArrayList<Group> getChildren() {
		return this.children;
	}
	
	public void addChild(Group newChild) {
		this.children.add(newChild);
		newChild.setParent(this);
	}
	
	public Group getParent() {
		return this.parent;
	}
	
	public void setParent(Group newParent) {
		this.parent = newParent;
	}
	
	@Override
	public String toString() {
		return this.type.name() + "|" + this.text;
	}
}
