import java.util.concurrent.CyclicBarrier;

class E2 extends Production {
    E2(Vertex Vert, CyclicBarrier Barrier) {
        super(Vert, Barrier);
    }

    Vertex apply(Vertex T) {
        System.out.println("E2");

        double divider = T.m_a[0][0];

        for(int i = 0; i < 3; ++i) {
            T.m_a[0][i] /= divider;
        }

        T.m_b[0] /= divider;

        for(int i = 1; i < 3; ++i) {
            double multiplier = T.m_a[i][0];
            T.m_a[i][0] = 0;
            for(int j = 1; j < 3; ++j) {
                T.m_a[i][j] -= multiplier * T.m_a[0][j];
            }
            T.m_b[i] -= multiplier * T.m_b[0];
        }
        return T;
    }
}