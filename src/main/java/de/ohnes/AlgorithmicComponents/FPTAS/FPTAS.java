package de.ohnes.AlgorithmicComponents.FPTAS;

import de.ohnes.util.Instance;

public interface FPTAS {
    public boolean solve(Instance I, double d, double epsilon);
}
