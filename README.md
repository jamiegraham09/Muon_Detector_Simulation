# Muon_Detector_Simulation

This program allows the user to simulate a Muon Detector with a specified number of Muons at a specified starting energy in MeV.

To get the program to work the user needs to change the path for which they want the two, final, comma seperated value file to 
save to this can be located in the 'Histogram' class under the method 'writeToDisk'. 

If the user does not specify the file path before running the program then it will not save the data for simulated muon paths.

The data can be observed in Microsoft Excel by plotting the x-postion or Bin value against both energy and y-value depending 
on which comma seperated value file was opened.

The user can also add aditional detector materials and create instances to do this. The detector is currently made from Iron 
this is a dense material that Muons will interact with.
