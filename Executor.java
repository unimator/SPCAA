import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

class Executor extends Thread {
    public synchronized void run() {
        Vertex S = new Vertex(null, null, null, "S");
        try {

            int k = 3;

            List<Vertex> vertices = new LinkedList<Vertex>();
            CyclicBarrier barrier = new CyclicBarrier(1 + 1);
            P1 p1 = new P1(S, barrier);
            p1.start();
            barrier.await();

            vertices.add(p1.m_vertex);

            for(int i = 1; i < k - 1; ++i ) {
                barrier = new CyclicBarrier(vertices.size() * 2 + 1);
                List<Vertex> updatedVertices = new LinkedList<Vertex>();
                for(Vertex vertex : vertices) {
                    //[(P2)1(P2)2]
                    P2 p2a = new P2(vertex.m_left, barrier);
                    P2 p2b = new P2(vertex.m_right, barrier);
                    p2a.start();
                    p2b.start();
                    updatedVertices.add(p2a.m_vertex);
                    updatedVertices.add(p2b.m_vertex);
                }
                vertices = updatedVertices;
                barrier.await();
            }

            //[(P2)3(P2)4(P3)5(P3)6]
            barrier = new CyclicBarrier(vertices.size() * 2 + 1);
            List<Vertex> leaves = new LinkedList<Vertex>();
            for(Vertex vertex : vertices) {
                P3 p3Left = new P3(vertex.m_left, barrier);
                P3 p3Right = new P3(vertex.m_right, barrier);
                p3Left.start();
                p3Right.start();
                leaves.add(p3Left.m_vertex);
                leaves.add(p3Right.m_vertex);
            }
            barrier.await();

            barrier = new CyclicBarrier(leaves.size() + 1);
            A1 localMatA1 = new A1(leaves.get(0), barrier);
            localMatA1.start();
            for(int i = 1; i < leaves.size() - 1; ++i) {
                A localMat = new A(leaves.get(i), barrier);
                localMat.start();
            }
            AN localMatAN = new AN(leaves.get(leaves.size() - 1), barrier);
            localMatAN.start();

            Set<Vertex> verticesSet = new HashSet<Vertex>();
            for(Vertex vertex : leaves) {
                verticesSet.add(vertex.m_parent);
            }

            do {
                barrier = new CyclicBarrier(verticesSet.size() + 1);
                for(Vertex vertex : verticesSet) {
                    A2 mergedMat = new A2(vertex, barrier);
                    mergedMat.start();
                }
                barrier.await();
                barrier = new CyclicBarrier(verticesSet.size() + 1);
                for(Vertex vertex : verticesSet) {
                    E2 gaussElimMat = new E2(vertex, barrier);
                    gaussElimMat.start();
                }
                barrier.await();
                Set<Vertex> updatedVerticesSet = new HashSet<Vertex>();
                for(Vertex vertex : verticesSet) {
                    updatedVerticesSet.add(vertex.m_parent);
                    System.out.println(Arrays.toString(vertex.m_x));

                }
                verticesSet = updatedVerticesSet;

            } while (verticesSet.size() > 1);

            //[(Aroot)]
            barrier = new CyclicBarrier(1 + 1);
            Aroot mergedRootMat = new Aroot(p1.m_vertex, barrier);
            mergedRootMat.start();
            barrier.await();

            barrier = new CyclicBarrier(1 + 1);
            Eroot eroot = new Eroot(p1.m_vertex, barrier);
            eroot.start();
            barrier.await();

            System.out.println(Arrays.toString(eroot.m_vertex.m_x));

            vertices = new LinkedList<Vertex>();
            vertices.add(p1.m_vertex);

            for(int i = 0; i < k - 1; ++i) {
                barrier = new CyclicBarrier(vertices.size() + 1);
                for(Vertex vertex : vertices) {
                    BS backwardSub = new BS(vertex, barrier);
                    backwardSub.start();
                }
                barrier.await();
                List<Vertex> updatedVertices = new LinkedList<>();
                for(Vertex vertex : vertices) {
                    updatedVertices.add(vertex.m_left);
                    updatedVertices.add(vertex.m_right);
                }
                vertices = updatedVertices;
            }

            System.out.println(leaves.get(0).m_x[1] + " ");
            for(int i = 0; i < leaves.size(); ++i) {
                System.out.println(leaves.get(i).m_x[2] + " ");
            }

            //test
            if (p1.m_vertex.m_a[0][0] == 1.0 & p1.m_vertex.m_a[0][1] == -0.5 && p1.m_vertex.m_a[0][2] == -0.5 &
                    p1.m_vertex.m_a[1][0] == 0.0 & p1.m_vertex.m_a[1][1] == 1.0 && p1.m_vertex.m_a[1][2] == 0.0 &
                    p1.m_vertex.m_a[2][0] == -0.5 & p1.m_vertex.m_a[2][1] == 0.0 && p1.m_vertex.m_a[2][2] == 0.5 &
                    p1.m_vertex.m_b[0] == 0.0 & p1.m_vertex.m_b[1] == 0.0 && p1.m_vertex.m_b[2] == 0.25)
                System.out.println("OK");

        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
