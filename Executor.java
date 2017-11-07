import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

 class Executor extends Thread {
 public synchronized void run() {
 Vertex S = new Vertex(null,null,null,"S");
 try {
 //[(P1)]
 CyclicBarrier barrier = new CyclicBarrier(1+1);
 P1 p1 = new P1(S,barrier);
 p1.start();
 barrier.await();
 //[(P2)1(P2)2]
 barrier = new CyclicBarrier(2+1);
 P2 p2a = new P2(p1.m_vertex.m_left,barrier);
 P2 p2b = new P2(p1.m_vertex.m_right,barrier);
 p2a.start();
 p2b.start();
 barrier.await();
 //[(P2)3(P2)4(P3)5(P3)6]
 barrier = new CyclicBarrier(4+1);
 P3 p3c = new P3(p2a.m_vertex.m_left,barrier);
 P3 p3d = new P3(p2a.m_vertex.m_right,barrier);
 P3 p3e = new P3(p2b.m_vertex.m_left,barrier);
 P3 p3f = new P3(p2b.m_vertex.m_right,barrier);
 p3c.start();
 p3d.start();
 p3e.start();
 p3f.start();
 barrier.await();
 // MULTI-FRONTAL SOLVER ALGORITHM
 //[(A1)(A)1(A)2(AN)]
 barrier = new CyclicBarrier(4+1);
 A1 localMat1 = new A1(p3c.m_vertex, barrier);
 A localMat2 = new A(p3d.m_vertex, barrier);
 A localMat3 = new A(p3e.m_vertex, barrier);
 AN localMat4 = new AN(p3f.m_vertex, barrier);
 localMat1.start(); localMat2.start(); localMat3.start(); localMat4.start(); 
 barrier.await();
 //[(A2)1(A2)2(A2)3]
 barrier = new CyclicBarrier(2+1);
 A2 mergedMat1 = new A2(p2a.m_vertex, barrier);
 A2 mergedMat2 = new A2(p2b.m_vertex, barrier);
 mergedMat1.start(); mergedMat2.start(); 
 barrier.await();
 //[(E2)1(E2)2(E2)3]
 barrier = new CyclicBarrier(2+1);
 E2 gaussElimMat1 = new E2(p2a.m_vertex, barrier);
 E2 gaussElimMat2 = new E2(p2b.m_vertex, barrier);
 gaussElimMat1.start(); gaussElimMat2.start(); 
 barrier.await(); 
 //[(Aroot)]
 barrier = new CyclicBarrier(1+1);
 Aroot mergedRootMat = new Aroot(p1.m_vertex, barrier);
 mergedRootMat.start();
 barrier.await();
 
 //test
 if( p1.m_vertex.m_a[0][0]==1.0 & p1.m_vertex.m_a[0][1]==-0.5 && p1.m_vertex.m_x[0][2]==-0.5 &
   p1.m_vertex.m_a[1][0]==0.0 & p1.m_vertex.m_a[1][1]==1.0 && p1.m_vertex.m_x[1][2]==0.0 &
   p1.m_vertex.m_a[2][0]==-0.5 & p1.m_vertex.m_a[2][1]==0.0 && p1.m_vertex.m_x[2][2]==0.5 &
   p1.m_vertex.m_b[0]==0.0 & p1.m_vertex.m_b[1]==0.0 && p1.m_vertex.m_b[2]==0.25 ) System.out.println("OK");

  } catch (InterruptedException | BrokenBarrierException e) {
 e.printStackTrace();
 }
 }
 } 
