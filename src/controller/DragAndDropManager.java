package controller;

import javafx.scene.Node;


/**
 * @author florian campanella, Thomas Rambaldi, Nicolas Leotier
 * gestion du drag and drop avec une classe abstraite  DragAndDropManager
 */
public abstract class DragAndDropManager {
	
	Node node;
	
	/**
	 * Constructeur
	 * @param node
	 */
	public DragAndDropManager(Node node) {
		this.node = node;
	}


	/**
	 * defines a function to be called when drag gesture has been detected.
	 * this is the rigth place to start drag and drop operation. 
	 */
	public abstract void onDragDetected();
	
	
	/**
	 * defines a function to be called when drag gesture progresses within this node
	 */
	public abstract void onDragOver ();
	
	
	/**
	 * defines a function to be called when drag gesture enters this node
	 */
	public abstract void onDragEntered ();
	
	
	/**
	 * defines a function to be called when drag gesture exits this node
	 */
	public abstract void onDragExited ();
	
	
	/**
	 * Defines a function to be called when the mouse button is released on this node during drag and drop 
	 * gesture. 
	 */
	public abstract void onDragDropped ();
	
	
	/**
	 * defines a function to be called when this node is a drag and drop gesture source after its data has been droped on a drop target.
	 */
	public abstract void onDragDone();
	
	
	/**
	 * function for called every Drag and Drop methodes
	 */
	public void onDragAndDropEvent() {
		onDragDetected();
		onDragOver();
		onDragEntered();
		onDragExited();
		onDragDropped();
		onDragDone();
	}
	
}
