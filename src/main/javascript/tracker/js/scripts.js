$(document).ready(function() {
    var paintMap = function(map, i) {

        $('#main').html('');
        _.each(map, function(row) {
            $('#main').append('<div class="row">');

            _.each(row, function(column) {
                var c = 0;
                if(column === 1) {
                    c = "me";
                } else if (column === 2) {
                    c = "enemy"
                } else if(column === 3) {
                    c = "intersect"
                }
                $('#main').append('<div class="cell '+c+'"></div>');
            });
            $('#main').append('</div>');
        });

        $('.steps-left').html('Steps left: ' + i);
    };

    $('.play').click(function(e) {
        var maps = JSON.parse($('#input').val());
        console.log(maps);
        var i = 0;
        var interval = setInterval(function() {

            paintMap(maps[i]);
            i++;

            if(i === maps.length)
                clearInterval(interval);

        }, 300);
    });

    $('.fileinput').click(function(e) {

        $.getJSON('output.txt', function(response) {
            console.log(response);
            var i = 0;
            var interval = setInterval(function() {

                paintMap(response[i], response.length - i - 1);
                i++;

                if(i === response.length)
                    clearInterval(interval);

            }, 100);
        });
    })

});
