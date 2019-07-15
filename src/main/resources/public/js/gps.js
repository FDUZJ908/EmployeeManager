function getLocation(callback) {
    navigator.geolocation.getCurrentPosition(function (pos) {
        callback(pos.coords.longitude.toFixed(5), pos.coords.latitude.toFixed(5));
    }, function (error) {
        window.alert("请打开GPS！");
    });
}
