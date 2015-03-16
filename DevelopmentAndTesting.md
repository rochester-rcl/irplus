#Development and Testing of IR+

### IR+  Development and Testing at the University of Rochester ###
At the University of Rochester IR+ is primarly developed and tested on windows machines - but is released to UNIX for pre-production testing and as our production environment.

IR+ has over 500 automated tests (and growing) that are run prior to deployment and user testing.  We use a testing package called TestNG very similar to JUnit.  The IR+ sofware is not deployed until all automated tests have passed.  This ensures a base level of quality across the code base and prevents users from having to deal with issues that should have been caught in the development phase.

### Pre-Production Deployment ###

Once the automated testing is completed we depoly the software to a pre-production machine (UNIX OS) for use case and load testing.  We have a set list of Use Cases/Test Scripts that are then completed by users on the Pre-Production machine to help catch any other issues and veryify that the system performs with a given set of data (Over 11,000 publications).

### Production Deployment and open source release ###

Once the Use Cases have been successfuly tested, IR+ is then built and deployed to our production machine.  We build the system on a windows machine and then deploy to a UNIX environment.  The important point here is it doesn't matter where the build takes place.  We then run the IR+ in production at the University of Rochester for about one to two weeks then do a build for the community.  This doesn't catch everything but allows us to verify a certian level of quality is met before releasing to the public.

### Updates and Fixes ###

Based on priority and severity, fixes may be released very quickly.  Security issues that are found are tested and released as soon as possible.  Feature enhancements and non-blocking issues are evaulated and prioritized to be released following the next development cycle.