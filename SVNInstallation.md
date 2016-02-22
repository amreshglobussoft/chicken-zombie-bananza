# Instructions #

Checkout Instructions
  1. Download Tortoise SVN from http://tortoisesvn.net/
  1. Checkout the project from https://chicken-zombie-bananza.googlecode.com/svn/trunk/ to somewhere on your computer
  1. Use your Google email address and your Google Code password; it's different from your Google password!, you can get it at http://code.google.com/hosting/settings
  1. Make sure you have Eclipse installed with the Android SDK, with the Google APIs Version 8 downloaded
  1. Start up Eclipse and create a new Android project and 'Create a project from existing source and change the location to the 'project' directory of the checkout folder
    * Make sure Google API's Version 8 is selected in the build target
  1. Run the application in the Android Emulator to verify that everything is working

Commit Instructions
  1. ALWAYS do an SVN Update before you commit to make sure you have the latest revision.
  1. Get your Google Code SVN password from https://code.google.com/hosting/settings
  1. In the folder where you checked out the source, right click and select 'SVN Commit'
  1. Make sure all the files you want to commit are selected and that the ones you don't are not. Type a brief message explaining what this commit is about.
  1. Press OK and enter your Gmail address as the user name and the password you just got. TortoiseSVN will inform you if there is a problem with the commit.

# Notes #

Rules on making a commit
  1. Don't commit if it will break the build!
  1. Make sure you are committing all the files you need to.
  1. You don't need to write a paragraph, but a short and sweet comment of what you did will help others see what you have done.
  1. When working on a larger task, logically subdivide the task into subtasks and make commits when those tasks are complete. Don't wait until you are done to commit.