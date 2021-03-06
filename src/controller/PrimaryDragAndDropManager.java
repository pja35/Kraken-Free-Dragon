package controller;

import view.implementation.ControlTower;
import view.implementation.PrimaryGraphicExpression;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.control.Label;


/**
 * @author florian Campanella, Thomas Rambaldi, Nicolas Leotier
 * etend DragAndDropManager
 * implemente les methodes de drag and drop pour le PrimaryGraphicExpression
 */
public class PrimaryDragAndDropManager extends DragAndDropManager{

	Label label;
	private PrimaryGraphicExpression primary;
	private ControlTower tower;
	
	
	/**
	 * constructeur PrimaryDragAndDropManager
	 * @param node : le node
	 * @param label : le label
	 * @param primary : le primary
	 * @param tower : tower
	 */
	public PrimaryDragAndDropManager(Node node, Label label, PrimaryGraphicExpression primary, ControlTower tower) {
		super(node);
		this.label = label;
		this.primary = primary;
		this.tower = tower;
	}

	
	/**
	 * @see DragAndDropManager
	 */
	@Override
	public void onDragDetected() {
		primary.setOnDragDetected(new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent event) {
		        /* drag was detected, start a drag-and-drop gesture*/
		        Dragboard db = primary.startDragAndDrop(TransferMode.MOVE);
		        //Petite icone
//		        db.setDragView(new Text(primary.getText().getText()).snapshot(null, null), event.getX(), event.getY());
		        ClipboardContent content = new ClipboardContent();
		        content.putString(primary.getText().getText());
		        DragAndDropMemory.memory.setSource(primary);
		        db.setContent(content);
		        event.consume();
		    }
		});
	}
	
	/**
	 * @see DragAndDropManager
	 */
	@Override
	public void onDragOver () {
		node.setOnDragOver(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		        /* data is dragged over the target */
		            /* allow for moving */
	            event.acceptTransferModes(TransferMode.MOVE);
		        event.consume();
		    }
		});
	}
	
	/**
	 * @see DragAndDropManager
	 */
	@Override
	public void onDragEntered () {
		node.setOnDragEntered(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		    /* the drag-and-drop gesture entered the target */
		    	DragAndDropMemory.memory.setTarget(primary);
		    	event.consume();
		    }
		});
	}
	
	/**
	 * @see DragAndDropManager
	 */
	@Override
	public void onDragExited () {
		node.setOnDragExited(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		        /* mouse moved away, remove the graphical cues */
		        event.consume();
		    }
		});
	}	
	
	/**
	 * @see DragAndDropManager
	 */
	@Override
	public void onDragDropped () {
		node.setOnDragDropped(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		        /* data dropped */
		        Dragboard db = event.getDragboard();
		        boolean success = false;
		        if (db.hasString()) {
		           success = true;
		        }
		        event.setDropCompleted(success);
		        event.consume();
		     }
		});
	}
	
	/**
	 * @see DragAndDropManager
	 */
	@Override
	public void onDragDone() {
		primary.setOnDragDone(new EventHandler<DragEvent>() {
		    public void handle(DragEvent event) {
		        /* the drag and drop gesture ended */
		        if (event.getTransferMode() == TransferMode.MOVE) {
		        	DragAndDropMemory.memory.notifyDragAndDrop();
		        }
		        event.consume();
		    }
		});
	}
	
}
