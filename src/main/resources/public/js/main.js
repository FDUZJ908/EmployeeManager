
function exportTo(type) {

	$('.table').tableExport({
		filename: '阿斯顿负',
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