// Recursively stitch images in ImageJ from Nikon - NIS-Elements

function pad (a, left) { 
	while (lengthOf(""+a)<left) a="0"+a; 
	return ""+a; 
} 

function searchAndStitch(dir) {
  list = getFileList(dir);
  if(list.length == 0) {
  	return;
  }
  if (list.length > 3 && startsWith(list[3], "tile_")) {
  	print("Found directory to stitch: " + dir);
  	dirname = File.getName(dir);
  	parent = File.getParent(dir);
  	print("Parent Directory: " + parent);
	xy = newArray(2);
	xy[0] = 1; 
	xy[1] = 1;
	while(File.exists(dir + 'tile_x' + pad(xy[0],3) + '_y001.tif')){
		xy[0]++;
	}
	while(File.exists(dir + 'tile_x001_y' + pad(xy[1],3) + '.tif')){
		xy[1]++;
	}
	// check if all images are there
	all_files_present = true;
	for(i = 1; i < xy[0]; i++){
		for(j = 1; j < xy[1]; j++){
			if(!File.exists(dir + 'tile_x' + pad(i,3) + '_y' + pad(j,3) + '.tif')){
				all_files_present = false;
			}
		}
	}
	xy[0]--;
	xy[1]--;
	if(all_files_present){
		print("Directory " + dir + " has grid dimension (" + xy[0] + "," + xy[1] + ")");	
		run("Grid/Collection stitching", "type=[Filename defined position] order=[Defined by filename         ] grid_size_x=" + xy[0] + " grid_size_y="+xy[1]+" tile_overlap=15 first_file_index_x=1 first_file_index_y=1 directory=[" + dir + "] file_names=tile_x{xxx}_y{yyy}.tif output_textfile_name=TileConfiguration.txt fusion_method=[Linear Blending] regression_threshold=0.30 max/avg_displacement_threshold=0 absolute_displacement_threshold=0 computation_parameters=[Save memory (but be slower)] image_output=[Fuse and display]");
	
		destDir = parent + "/ImageJ_Simple_Stitch/";
		File.makeDirectory(destDir);
		windowList = getList("image.titles");
		if(windowList.length!=0){
			saveAs("Tiff", destDir + dirname + '.tif');
			saveAs("Jpeg", destDir + dirname + '.jpg');
		}
		close();		
	}

  }
  else{
  	for(i = 0; i < list.length; i++){
  		if(endsWith(list[i], "/")){
  			print("Recursing into: " + list[i]);
  			searchAndStitch(dir + list[i]);
  		}
  	}
  }
}
	
masterdir = getDirectory("Choose a Main Directory");
searchAndStitch(masterdir);
