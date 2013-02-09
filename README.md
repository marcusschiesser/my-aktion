My-Aktion, example application for the german book "Java EE 7 Workshop" published by dpunkt.verlag
More information at: http://www.dpunkt.de/buecher/3877/workshop-java-ee-7.html
User Maven 3.0.4 (http://maven.apache.org/download.cgi) for building.
This example contains of two different applications my-aktion and my-aktion-monitor which
must be build independently and run on different application servers.


MY-AKTION
=========

The main application allows organizers of nonprofit campaigns (German: Aktion) to create online fundraising forms
for their projects. The organizer can embed these forms into their own website. This allows the
organizer to raise money for his campaign. Each organizer can create forms for multiple campaigns.
The application takes care of the generation of the forms and the management of the campaigns and donations.
This app was tested under JBoss 7.1.1


MY-AKTION-MONITOR
=================

The monitor is a simple application that takes heavy usage of new Java EE 7 features 
like WebSocket and REST client and therefore only runs under Glassfish 4.0. 
To run it get the latest promoted build at http://dlc.sun.com.edgesuite.net/glassfish/4.0/promoted/
It sets up a SOAP and REST based (to demonstrate both technologies) communication with the main app
to retrieve the donations for a selected campaign.


LICENSED UNDER MIT-LICENSE
==========================

Copyright (c) 2013 Marcus Schiesser and Martin Schmollinger

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions
of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
IN THE SOFTWARE.
