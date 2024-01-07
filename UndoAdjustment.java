/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package photoeditor10;

/**
 *
 * @author jiks
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class UndoAdjustment {
    private Stack<ImageIcon> undoStack;

    public UndoAdjustment() {
        undoStack = new Stack<>();
    }

    public void pushUndo(ImageIcon icon) {
        undoStack.push(icon);
    }

    public ImageIcon popUndo() {
        if (!undoStack.isEmpty()) {
            return undoStack.pop();
        }
        return null; // No image to undo to
    }
}


