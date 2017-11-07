import java.lang.*;

class TestSolution {
	TestSolution() {
	}
	//test the tree
	boolean test(Vertex v, double Beg, double End) {
      boolean result;		
      boolean ret=true;		
	  if(v.m_left!=null ) {
	  	if(v.m_left.m_left==null) {
//  	  	  if(v.m_left.m_left.m_left==null) {
            System.out.println("["+v.m_x[0]+","+v.m_x[2]+"]vs"+"["+Beg+","+End+"]");
            if(Math.abs(v.m_x[0]-Beg)>0.00000001 || Math.abs(v.m_x[2]-End)>0.00000001)ret=false;
//  	  	  }
	  	}
	  }
      if(v.m_left!=null) {
	    result=test(v.m_left,Beg,Beg+(End-Beg)*0.5);
		if(result==false)ret=false;
	  }
      if(v.m_right!=null) {
	    result=test(v.m_right,Beg+(End-Beg)*0.5,End);
		if(result==false)ret=false;
	  }
	  return ret;
	}
}

