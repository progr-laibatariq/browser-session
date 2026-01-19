package com.dsa.browsersession.command;

public interface Command {
    void execute();
    void undo();
    String name();
}
