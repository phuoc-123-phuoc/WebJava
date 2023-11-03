var trackRecordCount;
$(document).ready(function(){
	trackRecordCount = $(".hiddenTrackId").length;

	$("#tracklist").on("click",".linkRemoveTrack",function(e){
		e.preventDefault();
		deleteTrack($(this));
		updateTrackCountNumbers();
	});
	$("#track").on("click","#linkAddTrack",function(e){
		e.preventDefault();
		addNewTrackRecord();
	});
	$("#tracklist").on("change",".dropDownStatus",function(e){
		dropDownList = $(this);
		rowNumber = dropDownList.attr("rowNumber");
		selectedOption = $("option:selected",dropDownList);
		
		defaultNode = selectedOption.attr("defaultDescription");
		$("#trackNote" + rowNumber).text(defaultNode);
	});
	
});

function deleteTrack(link){
	rowNumber = link.attr('rowNumber');
	$("#rowTrack" + rowNumber).remove();
	$("#emptyLine" + rowNumber).remove();
}

function updateTrackCountNumbers(){
	$(".divCountTrack").each(function (index,element){
		element.innerHTML = "" + (index + 1);
	});
}	

function addNewTrackRecord(){
	htmlCode= generateTrackCode();
	$("#tracklist").append(htmlCode);
}

function generateTrackCode(){
	nextCount = trackRecordCount+ 1;
	trackRecordCount++;
	rowId = "rowTrack" + nextCount;
	emptyLineId = "emptyLine" + nextCount;
	trackNoteId = "trackNote" + nextCount;
	currentDateTime = formatCurrentDateTime();
	
	htmlCode =`
		<div class="row border rounded p-1" id="${rowId}">
			<input type="hidden" name="trackId" value="0" class="hiddenTrackId" /> 
					<div class="col-2">
						<div class="divCountTrack">${nextCount}</div>
								<div class="mt-1">
									<a class="fas fa-trash icon-dark linkRemoveTrack" href="" rowNumber="${nextCount}"></a>
								</div>	
					</div>
					
					<div class="col-10">
						<div class="form-group row">
							<label class="col-form-label">Time: </label>
							<div class="col">
								<input type="datetime-local" name="trackDate" value="${currentDateTime}"
										class="form-control" required
										style="max-width: 300px"/>
							</div>
						</div>
						<div class="form-group row">
							<label class="col-form-label">Status: </label>
							<div class="col">
								<select name="trackStatus" class="form-control dropDownStatus" required="required" style="max-width: 150px"
										rowNumber="${nextCount}>
										`;
	htmlCode += $("#trackStatusOptions").clone().html();
	htmlCode += `
										
									
								</select>
							</div>
						</div>
						<div class="form-group row">
							<label class="col-form-label">Notes: </label>
							<div class="col">
								<textarea rows="2" cols="10" class="form-control" name="trackNotes" style="max-width: 300px"
										id="${trackNoteId}" required="required"
									>${trackNoteId}
								</textarea>
							</div>
						</div>
					</div>
			</div>
			<div th:id="${emptyLineId}" class="row">&nbsp;</div>
	`;
	return htmlCode;
}

function formatCurrentDateTime() {
  var currentDate = new Date();
  var year = currentDate.getFullYear();
  var month = currentDate.getMonth() + 1;
  var day = currentDate.getDate();
  var hour = currentDate.getHours();
  var minute = currentDate.getMinutes();
  var second = currentDate.getSeconds();

  if (month < 10) {
    month = "0" + month;
  }
  if (day < 10) {
    day = "0" + day;
  }
  if (hour < 10) {
    hour = "0" + hour;
  }
  if (minute < 10) {
    minute = "0" + minute;
  }
  if (second < 10) {
    second = "0" + second;
  }

  return year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second;
}

