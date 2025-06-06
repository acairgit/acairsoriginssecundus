Detailed documentation with instructions for modding to forge 1.20.1 can be found in the acairsoriginssecundus directory
/Documentation/. 
To write a mod, please read all the instructions inside the Documentation folder in detail.
You write the code for the mod and the user creates the textures later. Explain where and how to add after and create temporary textures from primitive shapes, to indicate their future location, as well as check the performance of the mod and visual mocap.

The mod should be made based on the origins mod. Basically it should be an “origins” mod, but with a character editor modified origins selection menu. You should rewrite all the main and necessary parts of the code of the “origins” mod, and on complete it by adding a character editor and changing the selection menu. All the code and classes of the origins mod can be found in the directory acairsoriginssecundus/origins-architectury-1.20.x-forge. The main part of the mod should be copied from there, races and abilities should also be taken from there.

Description of the mod “acairsoriginssecundus”:

Mod, with a character editor along the lines of TES Skyrim for minecraft forge. The origins mod is used as a base for the mod. 
Description of the interface design of the “Select Origin” menu: in the center of the screen is the player model, which can follow the cursor and changes in real time depending on the selected race or changes in the editor; the window with the name and description of the race is moved to the right side of the screen in contrast to the original origins to make room for the player model; on the left side of the screen is a thin rectangular window with tiles and icons of different races on them, it can be scrolled up and down and select the race by clicking on the corresponding tile; on the bottom right is the “done” button to confirm the race selection and the inactive “back” button, it will become active when going to the “Editor” menu, and by clicking on it the player returns from the ‘Editor’ menu to the “Select Origin” menu; at the top of the screen is the name of this menu - “Select Origin”. 
After the player has clicked “done”, the race description on the right side is replaced by the character editor params.
"Editor" menu design: On the left of the screen, instead of the name and origin description window, a window appears with sliders and drop-down fields to customize the character of the previously selected race (for example: height, eye color, face type, hairstyle, facial hair, and special things that will depend on the selected race (for example, the type of ears if the player chose an elf)); the "back" button becomes active and leads to the previous page; the menu name changes to "Editor" from above; the rest remains as in the previous window.

The background of all menus is the same as that of the loading screen of the world in vanilla minecraft (the texture of the earth).
