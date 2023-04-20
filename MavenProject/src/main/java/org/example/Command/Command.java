package org.example.Command;

import java.io.Serializable;

public interface Command<T> extends Serializable {
    T execute();
}
