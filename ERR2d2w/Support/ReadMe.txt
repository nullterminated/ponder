To use this framework, I would recommend you work with 
Project Wonder source in your workspace.  You can find 
instructions on how to do this on the wiki.  The patches 
folder in this directory contains patches for project 
wonder on which this framework depends.  You will need 
to apply these patches to your wonder source in order for 
everything to work correctly.  

The Project Templates.zip file is a compressed archive of 
project templates for use with this framework. See the 
readme file contained in that archive for more information.

The Templates folder contains eoGen templates that I use
for generating model classes.  They're pretty nice now,
but I'm still running into cases where they need tweaking.

An example of how to add Wonder's ajax to an existing 
framework's edit pages is included in the AjaxLook.zip 
archive. The example should launch and allow you to go
to an EditAddress configuration page to test out the
dependency observers that work between different 
property level components.

TODO

Some things are currently works in progress. I need to
revise the JavaMail stuff because I'm sure I'm using
that incorrectly at the moment.  I am also wanting to
do a better job of the direct actions and work in some
sort of automated SEO stuff.  Sometimes the RSS is a
bit wonky, but I'm not quite sure why. I'd like to
change how I'm handling the property level stuff in the
rss anyway. I need to separate out the the repetitions
from the components for lists/inspects similar to how
ERD2W does it.  I still need to add tabbed interfaces.
I'm considering adding some ajax-y things, but I think
I would rather start that in a new framework.  I need
to add nice calendars for day, week, and year view. I'd
like to add iCal support with that. I need to figure
out why the EditToOneRelationship component on a
EditRelationship page does not show new EO which is on
the other side of the relationship. I'd like to use
ERXQualifierTraversal in the methods that build up
qualifiers for list DA urls. I'd like to update the
derived counts to be less hackish and possibly work
across many-to-many joins. I need to update the localized
key printing strategy. There's a ERDPageRunner
something or other that looks handy for that. I need
to create a language link menu instead of just a
popup menu. I need to revisit the localized page titles.
I still need to fix the action assignments for the list
task and finally dump the old actiongroups. I need to
look at banner localizations again. I'd like to revise
the current html to be a bit better structured. I need
to fix the query boolean to use radio buttons instead of
just a popup menu. I'd like to add support for captioning
the podcasts. I need to look at ERAttachments url encoding
of file names in the HTML, since it doesn't seem to escape
ampersands and that causes pink error boxes in the XHTML.
Add headings to the error and instruction panels. Just
revise those anyway, and possibly dump put the errors
beside the appropriate form fields instead of just in
panels. Figure out a new way to capture height, width,
and duration of uploaded movie files since QT4J seems to
be failing with exceptions instead of just being
deprecated now. Add a new list repetition that does
'baseball cards' instead of a table. Add a 'tree' inspect
view that allows for threaded discussions and such. I
need to convert the rest of the WOSubmitButtons and
WOHyperlinks to R2DLinkButtons. I need to look at
R2DLinkButton again and see if I can get rid of the
string binding by using the hasChildrenElements() 
method from WODynamicGroup.

Yeah, just a few things to fix. :)