
function exportTo(type,name) {

	$('.table').tableExport({
		filename: name,
		format: type,
		cols: '1,2'
	});

}

function exportAll(type) {

	$('.table').tableExport({
		filename: 'table_%DD%-%MM%-%YY%-month(%MM%)',
		format: type
	});

}