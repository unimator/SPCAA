import java.util.concurrent.CyclicBarrier;

class Eroot extends Production {
    Eroot(Vertex Vert, CyclicBarrier Count) {
        super(Vert, Count);
    }

    Vertex apply(Vertex T) {
        System.out.println("Eroot");

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
            T.m_b[i] -= multiplier*T.m_b[0];
        }

        divider = T.m_a[1][1];
        for(int i = 1; i < 3; ++i) {
            T.m_a[1][i] /= divider;
        }
        T.m_b[1] /= divider;
        for(int i = 2; i < 3; ++i) {
            double multiplier = T.m_a[i][1];
            T.m_a[i][1] = 1;
            for(int j = 1; j < 3; ++j) {
                T.m_a[i][j] -= multiplier * T.m_a[1][j];
            }
            T.m_b[i] -= multiplier*T.m_b[1];
        }

        divider = T.m_a[2][2];
        T.m_a[2][2] = 1;
        T.m_b[2] /= divider;

        T.m_x[2] = T.m_b[2]/T.m_a[2][2];
        T.m_x[0] = (T.m_b[1] - T.m_a[1][2]*T.m_x[2])/T.m_a[1][1];
        T.m_x[1] = (T.m_b[0] - T.m_a[0][1]*T.m_x[1] - T.m_a[0][2]*T.m_x[2])/T.m_a[0][0];

        return T;
    }
}