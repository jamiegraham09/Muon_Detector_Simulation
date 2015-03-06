import java.io.*;
class EnergyLoss
{
private static double energyloss;

private static int Z;
private static double A;
private static double mumass=106;
private static double emass=0.511;
private static double rho;
private static double I;
public  EnergyLoss(String name, int Za, double Aa, double density)
{
    int size = (100000-30)/10;
    I=0.0000135*Za;
    String title = name;
    rho = density;
    Z=Za;
    A=Aa;

}
public double getEloss(double Energy)
{
    
    double p = Math.sqrt((Energy*Energy)-(mumass*mumass));
    double ELoss = 0.307*rho*(Z/A)*(1/(getbeta(p)*getbeta(p)))*(Math.log((2*emass*getbeta(p)*getbeta(p)*getgamma(p)*getgamma(p))/I)-(getbeta(p)*getbeta(p)));
    
    return ELoss;
}
public double getbeta(double p)
{
    double beta  = p/Math.sqrt((p*p)+(mumass*mumass));
    return beta;
}
public double getgamma(double p)
{
    double gamma= 1/Math.sqrt(1-(getbeta(p)*getbeta(p)));
    return gamma;
}
}