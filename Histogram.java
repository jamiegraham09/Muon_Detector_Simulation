import java.io.*;
class Histogram
{
        static PrintWriter screen = new PrintWriter( System.out, true);
    //These variables have class scope
    protected double binsize, binlow, binhigh;
    protected String title;
    protected int SIZE, underflow, overflow;
    int trial;
   
    int[] hist; //define integer array to store histogram
    
    // constructor method for the class Histogram
    public Histogram (String t, int bins, double binlo, double binhi)
    {
        //store the parameters in local variables to be used later
        //trial= 1000;
        title = t;
        SIZE = bins;
        binlow = binlo;
        binhigh = binhi;
        //calculate any variables that might be useful later
        binsize = (binhigh-binlow)/(double)SIZE;
        hist = new int[SIZE];
        underflow =0;
        overflow =0;
    }
    
    //------------------------------------------
    //instance method starts here
    //------------------------------------------
    public int getSize() { return SIZE;}
    //------------------------------------------

    //------------------------------------------
    public void fillh (double x)
    {
        if( x > binlow && x < binhigh )
        {
            //upate the correct bin
            int bin = (int)((x-binlow)/binsize);
            hist[bin]++; // add 1 to the bin
        }
        
        else
        {
            if (x <= binlow ) underflow++;
            if (x >= binhigh) overflow++;
            
        }
    }
    //------------------------------------------
    public String getTitle()
        {
            //returns the title of the histogram to the user 
            return title;
        }
    //------------------------------------------
    public int getContent(int nbin)
        {
        
            //returns the contents on bin 'nbin' to the user
            return hist[nbin];
        
        }
        
    public int getOverflow() 
    {
        return overflow;
    }
    
    public int getUnderflow()
    {
        return underflow;
    }
    
    public double getStatError(int nbin) throws IOException
    {
        
        double StatError = Math.sqrt(getContent(nbin));
        return StatError;
    }
    
    public void writeToDisk(String name) throws IOException  //private Method that is used to write to disk.
    {
        //this method is for writing to disk
        String filename  ="/Users/Jamie/Desktop/"+name+".csv";
        FileWriter file1= new FileWriter(filename); // creates the file on the A: drive
        PrintWriter outputFile = new PrintWriter(file1); //sends the output to file1
        //we choose to write the gile as a comma seperated file (.csv) so it can be opened in excel
        outputFile.println("Size ," + SIZE); // + ", Trials," + trial);
        outputFile.println("Binlow , " + binlow);
        outputFile.println("Binhigh ," + binhigh);
        outputFile.println("Binint , " + binsize);
        outputFile.println("Overflow, "+ overflow + ", Underflow ," +underflow);
        outputFile.println("Bin Number ,"+"Bin Centre ,"+"Number of hits, "+ "Statistical Error");
        
        //loop to write the contents of each bin to disk, one number at a time
        //together with the x-coordinate find the centre of each bin
        for (int n=0; n<= SIZE-1; n++)
        {
            double binCentre = binlow + binsize/2 + n*binsize; // finds the centre value of the bin.
            outputFile.println( n + ", "+ binCentre + ", "+ hist[n]+", " +getStatError(n)); //writes the value of n in column 1, bin centre in column 2, bin value in column 3, statistical error in column 4.
        }
       
        outputFile.close(); // close the output file.
        return;
    }
    
    /*public void writeMuon(int nsteps, double trackOfMuon[][], double actualMuonEnergy) throws IOException 
    {
        String filename  ="/Users/Jamie/Desktop/MuonTrack.csv";
        FileWriter file2= new FileWriter(filename); // creates the file on the A: drive
        PrintWriter outputFile = new PrintWriter(file2); //sends the output to file1
        
        for (int n=0; n<=nsteps-1; n++)
        {
        outputFile.println(nsteps+ ","+ trackOfMuon +","+ actualMuonEnergy);
        }
        
        outputFile.close(); // close the output file.
        return;  
    }*/
}
