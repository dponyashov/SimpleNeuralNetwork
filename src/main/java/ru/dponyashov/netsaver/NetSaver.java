package ru.dponyashov.netsaver;

import ru.dponyashov.nnet.NewNet;

public interface NetSaver {
    void save(NewNet net);
    NewNet load();
}
