import java.io.*;
import java.util.Random; 
class TrackMuon
/** an accurate 2D tracking of a high energy muon through iron with no magnetic field.*/
{
    // define the identifiers with class scope.
    static BufferedReader keyboard = new BufferedReader (new InputStreamReader(System.in)) ;
    static PrintWriter screen = new PrintWriter( System.out, true);

    static double inputEnergy, stepSize, ironThickness;
    static int numberOfMuons;
    // Instantiate the class Random, give 'value' class scope
    static Random value = new Random(); 

    static Histogram exitE = new Histogram("Muon exit from iron energy", 50,0,1000);
    static Histogram exitY = new Histogram("Muon final Y Position", 50, -6, 6);

    //notice defining Histograms here gives the reference pointer (exitE) class scope.
    //--------Class methods start here-------------------------------
    /**   Set the seed and get data from the keyboard */
    private static void getStartingConditions() throws IOException
    {
        // first set the seed for the random number generator so it always
        // produces the same sequence of random numbers
        long seed = 7894694;
        value.setSeed(seed); 
        screen.println(" Type in starting energy in MeV  ");
        inputEnergy = new Double(keyboard.readLine() ).doubleValue();
        
        screen.println(" Type in a step size in cm  ");
            stepSize = new Double(keyboard.readLine() ).doubleValue();
    
        screen.println(" Type in the thickness of iron, cm  "); 
        ironThickness = new Double(keyboard.readLine() ).doubleValue();

        screen.println(" Type in the number of muons to track  ");
        numberOfMuons = new Integer(keyboard.readLine() ).intValue();

        return;
    }
    //-------------------------------------------------------------------

    /**   Throw a gaussian distribution given the mean and sigma*/
    private static double gauss( double xmean, double sigma )
        {
        // Return a random number with a gaussian distribution  
        double newGauss, sum;
       
            sum=0;
            for (int n=0 ; n<=11; n++)
            {
              sum=sum + value.nextDouble();// use the class Random to make a number
            }
        newGauss = xmean + sigma*(sum -6);

        return newGauss;      
               
    }
    //------------------------------------------------------------
    /**   Allows output for each muon after the tracking has finished.*/
        private static void lookAtThisMuon(int nsteps, double [][] track, double finalE)
        {
        // Method to output some information about each muon
        double xlast,ylast;
        screen.println(" actual steps = " + nsteps);  
            xlast = track  [nsteps-1][0];
            ylast = track  [nsteps-1][1];
            screen.println(" last (x,y) of track =  ( " + xlast + " , " + ylast + ")" );
            screen.println(" energy of muon as it leaves the material = " + finalE +"  MeV");
            

        // make histograms  only if the muon left the material
            if (xlast >= ironThickness )
            {
              exitE.fillh(finalE);  // histogram the exit energy
              exitY.fillh(ylast); //historgram for y position 
            }
            return;
    }

    //------------ End of class methods --------------------------

        public static void main (String [] args ) throws IOException
        { 
        EnergyLoss iron = new EnergyLoss("iron", 26,55.85,7.87);
        MCS ironMS = new MCS("iron",26,55.85,7.87);

            int nsteps; // count number of steps 
            int nmax =200; // maximum allowed number of steps before we stop following a muon.
        // Define a 2D array to store the (x,y) pairs generated as track is followed.
        // allow enough space to store the hit positions on the counters.
        double [] [] trackOfMuon = new double[nmax +2 ] [2];// see page 169

            double actualMuonEnergy;
            double x; // x coordinate of muon , starts at x=0 just inside iron
            double y; // y coordinate of muon, introduced by multiple scattering
            double xnew,ynew;
        double theta; // current angular direction (radians) of muon
            double thetaT;// width of MCS distribution for a muon.

        // Define position and resolution of counters that detect the muon as it 
        // exits the iron.
        double xc1= ironThickness + 10; // x - coord of first counter after the iron
        double xc2 =ironThickness + 20;
        double xc3 = ironThickness + 30;
        double counterYcoordResolution = 2; // sigma of y coord resolution in cms.

        // Ask user for input data.
            getStartingConditions(); // Put all input requests in one method.
           
            
        // Start tracking each muon
        for (int n =1; n <= numberOfMuons; n++)
            {
            actualMuonEnergy =inputEnergy;
         
            x=0; //Set the initial starting position.
            y=0;
            nsteps=0;
            theta =0; // muon starts out parallel to x-axis
            screen.println("\n\n Start tracking muon  " + n + " ,energy =  " + actualMuonEnergy );
            // In this program we are working in units of cms.
        
            while ( x < ironThickness && nsteps < nmax ) // Note the 2 conditions here
            {
                // Step is the direction in the x-direction. If the muon is scattered by an angle
                // theta then the amount of material the muon travels through is d = step/cos(theta) 
                double step = Math.min( stepSize, ironThickness-x);
                // Ensure the final step just reaches the end of the iron.
            
                // Find width of MCS distribution for this muon travelling a distance stepSize 
                // through material.
                thetaT= ironMS.mcsTheta0(actualMuonEnergy, step);
                screen.println("ThetaT = " +thetaT);
                // Generate a random  angle with mean 0 with gaussian spread to add to current 
                // direction.
                theta= theta + gauss (0,thetaT);
                screen.println("theta Gauss = " + theta);
                
                double d = step/Math.cos(theta);
                screen.println("d = " + d);
                // Find energy loss going through d cm of material.
                actualMuonEnergy = actualMuonEnergy - iron.getEloss(actualMuonEnergy) *  d   ;

                // Warning: the above line assumes that the energy loss can be regarded as being
                // essentially constant for the muon travelling a distance  'step'.
                // If this is not true then it is necessary to change step.

                    if(actualMuonEnergy < 0)
                    {
                    screen.print(" Energy of muon goes negative.. abandon it");
                    break; // This causes the 'for' loop to terminate.
                    }

                xnew = x + step ; // calculate next (x,y) position.
                ynew = y + d*Math.sin(theta); 

                screen.println(" tracking.. nsteps , xnew,ynew = " + nsteps + "  " + xnew + "  " + ynew);
                screen.flush();
                
                /*EDITTED OUT*/
                //String anykey;
                //anykey = keyboard.readLine();// pause until any key is pressed.
                // Store these co-ordinates
                trackOfMuon  [nsteps] [0] = xnew;
                trackOfMuon  [nsteps] [1] = ynew;
                // Update coordinates in order to take the next step.
                x = xnew;
                y = ynew;

                nsteps++;
                // At this point will return to the start of the 'while' loop and take another step.

                if(nsteps == nmax) screen.println(" Too many steps for muon " + n + ",  abandon it");

            }
            // Finished tracking this muon, do some analysis on the results, and calculate hit
            // coordinates on the counters
            double thetaFinal=theta;
            screen.println("\nthetaFinal = " + thetaFinal);
            double xFinal = x; // final coords on track
            double yFinal = y;

            // Work out y-hit position on each counter and SMEAR it by the resolution.
            double yhitOnC1 = (xc1 - xFinal)*Math.tan(thetaFinal) + yFinal;
            yhitOnC1=gauss(yhitOnC1,counterYcoordResolution);
    
            double yhitOnC2= (xc2 - xFinal)*Math.tan(thetaFinal) +yFinal; 
            yhitOnC2 = gauss( yhitOnC2,counterYcoordResolution);
            
            double yhitOnC3= (xc3 - xFinal)*Math.tan(thetaFinal) +yFinal; 
            yhitOnC3 = gauss( yhitOnC3,counterYcoordResolution);

            // Add these coords into the array
            trackOfMuon  [nsteps +1] [0] = xc1;
            trackOfMuon  [nsteps +1] [1] = yhitOnC1;
            trackOfMuon  [nsteps +2] [0] = xc2;
            trackOfMuon  [nsteps +2] [1] = yhitOnC2;
            trackOfMuon  [nsteps +3] [0] = xc3;
            trackOfMuon  [nsteps +3] [1] = yhitOnC3;
     
            // pass the data to this method for any further processing

            lookAtThisMuon(nsteps,trackOfMuon,actualMuonEnergy); //
            // Now generate the next muon
            
            
        }
        

        exitY.writeToDisk("MuonYPosition");   
        exitE.writeToDisk("MuonEnergy");  
             screen.println("The File Muon Was written to file");
        // All muons done, finish program. If necessary, write histograms to disk at this point
    }
}