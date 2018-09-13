#!/bin/bash
main(){
	rm *.wav &> /dev/null
	rm *.mp4 &> /dev/null
	mkdir creations &> /dev/null
	mkdir temp &> /dev/null
	cd creations
	menu;
}

#prints the menu create screen
menu(){

	echo -e "\n\n=============================================================="
	echo "Welcome to NameSayer"
	echo "=============================================================="
	echo "Please select from one of the following options:"
	echo "(l)ist existing creations"
	echo "(p)lay an existing creation"
	echo "(d)elete an existing creation"
	echo "(c)reate a new creation"
	echo "(q)uit authoring tool"

	read -p "Enter a selection [l/p/d/c/q]:" userInput_menu
	case $userInput_menu in
		[l] ) listCreations2;;
		[p] ) playCreation;;
		[d] ) deleteCreation;;
		[c] ) createNew;;
		[q] ) exit;;
		* ) menu;;
	esac
}

checkInput(){
	fileExists=0
	for fname3 in *.mkv; do
		if [ "$fname3" == "$userInput.mkv" ]; then
			fileExists=1;
		fi
	done
}

#gives user choice to return to menu
promptToMenu(){
	echo
	read -n 1 -s -r -p "Press any key to return to menu"
	menu
}

#lists the creations in directory
listCreations(){

	echo
	echo -e "\nListing all creations:"
	exists=0
	
	shopt -s globstar
	shopt -s nullglob
	for fname in **/*.{mp4,mkv}; do
    		echo ${fname%%.mkv}
		exists=1;
	done

	if ((exists!=1)); then
		echo "No creations found"
	fi

}

#lists the creations in direcrtory then prompts return to menu
listCreations2(){
	listCreations
	promptToMenu
}

#plays a new creation
playCreation(){
	
	listCreations
	checkInput
	if ((exists==1)); then
		read -p "Please enter what to play:" userInput
		checkInput
			if ((fileExists==1)); then
				ffplay -autoexit "$userInput".mkv &> /dev/null
			else
				echo
				echo "That creation does not exist"
				playCreation
			fi
	fi
	promptToMenu
}

# deletes a creation
deleteCreation(){
	listCreations

	if ((exists==1)); then
		read -p "Please enter what to delete:" userInput
		checkInput
		if ((fileExists==1)); then
			confirmDelete
		else
			echo
			echo "That creation does not exist"
			deleteCreation
		fi
	fi

	promptToMenu
}


confirmDelete(){
	read -p "Are you sure you want to delete? [y/n]" userInput_DeleteConfirm
	case $userInput_DeleteConfirm in
		[y])	rm "$userInput".mkv; echo "$userInput deleted";;
		[n])	;;
		* ) confirmDelete;;
	esac
		
}

# creates a new creation
createNew(){
	
	listCreations
	creationName
	cd ..

	cd temp
	ffmpeg -f lavfi -i color=c=white:s=320x240:d=5 -vf \
	"drawtext=fontfile=/path/to/font.ttf:fontsize=30: \
 	fontcolor=black:x=(w-text_w)/2:y=(h-text_h)/2:text='$newName'" tempVid.mp4 &> /dev/null

	recordAudio
	cd ..

	ffmpeg -i temp/tempVid.mp4 -i temp/tempAud.wav -map 0:v -map 1:a creations/"$newName".mkv &> /dev/null

	echo "$newName" created
	cd temp
	rm tempVid.mp4
	rm tempAud.wav
	cd ..
	cd creations

	menu

}

# allows user to choose an unused name for their creation
creationName(){

	

	#input name and check if name exists
	read -p "Please enter the full, unique name for the new creation:" newName
	exists2=0
	for fname2 in *.mkv; do
		if [ "$fname2" == "$newName.mkv" ]; then
			exists2=1;
		fi
	done

	
	if ((exists2==1)); then
		echo "That name is taken"
		creationName
	fi
}

# records audio input
recordAudio(){

	read -n 1 -s -r -p "Press any key to begin recording"
	echo -e "\n5 Second Recording Started"

	ffmpeg -y -f alsa -i hw:1 -t 00:00:05 tempAud.wav &> /dev/null
	
	echo -e "\n5 Second Recording finished"	
	
	listenAudio
	recordAudioPrompt
}

recordAudioPrompt(){
	read -p "Do you wish to (k)eep or (r)edo the audio?" userInput_audioChoice
	case $userInput_audioChoice in 
		[r] ) recordAudio;;
		[k] ) ;;
		* ) recordAudioPrompt;;
	esac
}

# prompts the user if they want to listen to the audio thats been recordrd
listenAudio(){

	read -p "Do you wish to hear the recorded audio? [y/n] " userInput_ListenChoice
	case $userInput_ListenChoice in 
		[y] ) ffplay -autoexit tempAud.wav &> /dev/null; listenAudio;;
		[n] ) ;;
		* ) listenAudio
	esac
}

main;

