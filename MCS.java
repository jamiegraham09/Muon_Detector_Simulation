import java.io.*;
class MCS 

{
     static PrintWriter screen = new PrintWriter( System.out, true);
    
    double Q = 1; //charge
    double thickness = 1; //material thickness (cms)
    double massmu = 106; // muon mass MeV/c^2
    double masse = 0.511; // electron mass MeV/c^2
    final double c = 1;
    final double cms = 3e8;
    double atomic;
    double proton;
    double rho;
    String title;

    public MCS (String t, double Z, double A, double density) 
    {
       String title = t;
       proton = Z;
       atomic = A;
       rho = density;
    }
    public double getBeta(double p) 
    {
        double beta = p/Math.sqrt((p*p)+(massmu*massmu));
        return beta;
    }
    
    public double getGamma(double p) 
    {
        double b = getBeta(p);
        return 1/Math.sqrt(1-(b*b));
    }
    public double mcsTheta0 (double E, double step) //CHECK THESE ARGUMENTS 
    {
        //replaced t = 1 with step
        
        double p = Math.sqrt((E*E)-(massmu*massmu));
        screen.println("p "+ p);
        double X0= 716.4*(atomic/((proton*(proton+1))*(Math.log(287/Math.sqrt(proton))))); // g cm^-2
        screen.println("X0 "+ X0);
        double Xa= X0/rho;
        screen.println("Xa "+ Xa);
        double theta = 13.6/(getBeta(p)*p)*Q*Math.sqrt(step/Xa)*(1+(0.038*Math.log(step/Xa))); //radians
        screen.println("theta "+ theta);
        return theta;
    }
}
