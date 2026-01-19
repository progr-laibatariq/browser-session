package com.dsa.browsersession.command;

import com.dsa.browsersession.dsa.stack.CustomStack;

public final class CommandManager {
    private final CustomStack<Command> undoStack = new CustomStack<>();
    private final CustomStack<Command> redoStack = new CustomStack<>();

    public void executeCommand(Command cmd) {
        if (cmd == null) return;
        cmd.execute();
        undoStack.push(cmd);
        // clear redo on new action
        redoStack.clear();
    }

    public boolean undo() {
        Command cmd = undoStack.pop();
        if (cmd == null) return false;
        cmd.undo();
        redoStack.push(cmd);
        return true;
    }

    public boolean redo() {
        Command cmd = redoStack.pop();
        if (cmd == null) return false;
        cmd.execute();
        undoStack.push(cmd);
        return true;
    }

    public int undoSize() { return undoStack.size(); }
    public int redoSize() { return redoStack.size(); }
}
