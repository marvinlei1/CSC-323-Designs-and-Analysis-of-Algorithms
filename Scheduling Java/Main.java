import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;


class node {

  public int jobID, jobTime;
  public node next;

  public node(int jobID, int jobTime, node next) {
    this.jobID = jobID;
    this.jobTime = jobTime;
    this.next = next;
  }
}

class schedule {

  public int numNodes, numProcs, procUsed = 0, currentTime = 0, totalJobTime = 0, jobTimeAry[], adjMatrix[][], Table[][];
  public node OPEN;

  public schedule(int numNodes, int numProcs) {
    this.numNodes = numNodes;
    this.numProcs = numProcs;
    jobTimeAry = new int[numNodes + 1];
    for (int i = 0; i < numNodes + 1; i++) 
      jobTimeAry[i] = 0;
    
    adjMatrix = new int[numNodes + 1][numNodes + 1];
    for (int i = 0; i < numNodes + 1; i++)
      for (int j = 0; j < numNodes + 1; j++)
        adjMatrix[i][j] = 0;

    OPEN = new node(-9999, -9999, null);
  }

  public void loadMatrix(File inFile1) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(inFile1));
      String line;
      br.readLine();
      while ((line = br.readLine()) != null)
        adjMatrix[Integer.parseInt(line.substring(0, line.indexOf(" ")))][Integer.parseInt(line.substring(line.indexOf(" ") + 1, line.length()))] = 1;
      br.close();
    } catch (IOException e) {
    }

    Table = new int[numProcs + 1][totalJobTime + 1];
    for (int i = 0; i < numProcs + 1; i++)
      for (int j = 0; j < totalJobTime + 1; j++)
        Table[i][j] = 0;
  }

  public int loadJobTimeAry(File inFile2) {
    int time;
    try {
      BufferedReader br = new BufferedReader(new FileReader(inFile2));
      String line;
      br.readLine();
      while ((line = br.readLine()) != null) {
        time = Integer.parseInt(line.substring(line.indexOf(" ") + 1, line.length()));
        jobTimeAry[Integer.parseInt(line.substring(0, line.indexOf(" ")))] = time;
        totalJobTime += time;
      }
      br.close();
    } catch (IOException e) {
    }
    return totalJobTime;
  }

  public void setMatrix() {
    int count;
    for (int i = 1; i < numNodes + 1; i++) {
      count = 0;
      for (int j = 1; j < numNodes + 1; j++) 
        if (adjMatrix[j][i] == 1)
          count++;
      adjMatrix[0][i] = count;
    }

    for (int i = 1; i < numNodes + 1; i++) {
      count = 0;
      for (int j = 1; j < numNodes + 1; j++) 
        if (adjMatrix[i][j] == 1)
          count++;
      adjMatrix[i][0] = count;
    }

    for (int i = 1; i < numNodes + 1; i++)
      adjMatrix[i][i] = 1;

    adjMatrix[0][0] = numNodes;
  }

  public void printMatrix(File outFile2) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(outFile2, true));
      for (int i = 0; i < numNodes + 1; i++) {
        for (int j = 0; j < numNodes + 1; j++) {
          bw.write(adjMatrix[i][j] + " ");
          for (int k = String.valueOf(adjMatrix[i][j]).length(); k < String.valueOf(numNodes).length(); k++)
            bw.write(" ");
        }
        bw.write("\n");
      }
      bw.write("\n");
      bw.close();
    } catch (IOException e) {
    }
  }

  public int findOrphan() {
    for (int j = 1; j < numNodes + 1; j++)
      if (adjMatrix[0][j] == 0 && adjMatrix[j][j] == 1) {
        adjMatrix[j][j] = 2;
        return j;
      }
    return -1;
  }

  public void OpenInsert(node newNode) {
    node temp = OPEN;
    node temp2 = OPEN;
    if (temp.next == null) {
      temp.next = newNode;
    } else {
      while (temp2 != null) {
        if (temp2 == newNode) 
          return;
        else
          temp2 = temp2.next;
      }
      while (temp.next != null && newNode.jobTime < temp.next.jobTime)
        temp = temp.next;
      newNode.next = temp.next;
      temp.next = newNode;
    }
  }

  public void printOPEN(File outFile2) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(outFile2, true));
      node temp = OPEN;
      while (temp != null) {
        bw.write("(" + temp.jobID + ", " + temp.jobTime + ")");
        temp = temp.next;
      }
      bw.write("\n");
      bw.close();
    } catch (IOException e) {
    }
  }

  public int getNextProc(int currentTime) {
    for (int i = 1; i < numProcs + 1; i++) {
      if (Table[i][currentTime] == 0)
        return i;
    }
    return -1;
  }

  public void putJobOnTable(int availProc, int currentTime, int jobId, int jobTime) {
    int Time = currentTime, EndTime = Time + jobTime;
    while (Time < EndTime)
      Table[availProc][Time++] = jobId;
  }

  public void printTable(File outFile1, int currentTime) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(outFile1, true));
      bw.write("ProcUsed : " + procUsed + " currentTime : " + currentTime + "\n");
      bw.write("     ");
      for (int i = 0; i < totalJobTime + 1; i++) {
        bw.write(String.valueOf(i));
        for (int k = String.valueOf(i).length(); k < 3; k++) 
            bw.write(" ");
      }
      bw.write("\n");
      for (int i = 1; i <= numProcs; i++) {
        bw.write("P(" + i + ") ");
        for (int j = 0; j < totalJobTime + 1; j++) {
          bw.write(Table[i][j] == 0 ? "-" : String.valueOf(Table[i][j]));
          for (int k = String.valueOf(Table[i][j]).length(); k < 3; k++) 
            bw.write(" ");
        }
        bw.write("\n");
      }
      bw.close();
    } catch (IOException e) {
    }

  }

  public boolean checkCycle() {
    for (int i = 1; i < numProcs + 1; i++) {
      if (Table[i][currentTime] != 0)
        return false;
    }
    return OPEN.next == null && !isGraphEmpty();
  }

  public boolean isGraphEmpty() {
    return adjMatrix[0][0] == 0;
  }

  public void deleteJob(int jobID) {
    adjMatrix[jobID][jobID] = 0;
    adjMatrix[0][0]--;
    int j = 1;
    while (j <= numNodes) {
      if (adjMatrix[jobID][j] > 0)
        adjMatrix[0][j]--;
      j++;
    }
  }
}


class Main {
  public static void main(String[] args) {
    File inFile1, inFile2, outFile1, outFile2;
    inFile1 = new File(args[0]);
    inFile2 = new File(args[1]);
    outFile1 = new File(args[3]);
    outFile2 = new File(args[4]);
    int numNodes = 0, numProcs = 0;
    schedule sched;
    try {
      BufferedReader br = new BufferedReader(new FileReader(inFile1));
      numNodes = Integer.parseInt(br.readLine());
      br.close();
    } catch (IOException e) {
    }
    numProcs = Integer.parseInt(args[2]);
    if (numProcs <= 0) {
      System.out.println("need 1 or more processors");
      System.exit(0);
    } else if (numProcs > numNodes) {
      numProcs = numNodes;
    }
    sched = new schedule(numNodes, numProcs);
    sched.totalJobTime = sched.loadJobTimeAry(inFile2);
    sched.loadMatrix(inFile1);
    sched.setMatrix();
    sched.printMatrix(outFile2);
    int jobID;
    while (!sched.isGraphEmpty()) {
      while ((jobID = sched.findOrphan()) != -1) {
        node newNode = new node(jobID, sched.jobTimeAry[jobID], null);
        sched.OpenInsert(newNode);
        sched.printOPEN(outFile2);
      }
      int availProc;
      while ((availProc = sched.getNextProc(sched.currentTime)) > 0 && sched.OPEN.next != null && sched.procUsed < sched.numProcs) {
        if (availProc > 0) {
          sched.procUsed++;
          node newJob = sched.OPEN.next;
          sched.OPEN.next = sched.OPEN.next.next;
          sched.putJobOnTable(availProc, sched.currentTime, newJob.jobID, newJob.jobTime);
        }
      }
      sched.printTable(outFile1, sched.currentTime);
      boolean hasCycle = sched.checkCycle();
      if (hasCycle) {
        System.out.println("there is cycle in the graph!!!");
        System.exit(0);
      }
      sched.currentTime++;
      int proc = 1;
      while (proc <= sched.numProcs) {
        if (sched.Table[proc][sched.currentTime] <= 0 && sched.Table[proc][sched.currentTime - 1] > 0) {
          jobID = sched.Table[proc][sched.currentTime - 1];
          sched.deleteJob(jobID);
        }
        sched.printMatrix(outFile2);
        proc++;
      }
      sched.procUsed = 0;
    }
    sched.printTable(outFile1, sched.currentTime);
  }
}