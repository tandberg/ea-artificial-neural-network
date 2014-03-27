$(document).ready(function() {
    var paintMap = function(map) {

        $('#main').html('');
        _.each(map, function(row) {
            $('#main').append('<div class="row">');

            _.each(row, function(column) {
                $('#main').append('<div class="cell '+column+'"></div>');
            });

            $('#main').append('</div>');

        });
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

        }, 500);
    });

    $('.fileinput').click(function(e) {

        $.getJSON('output.txt', function(response) {
            console.log(response);
            var i = 0;
            var interval = setInterval(function() {

                paintMap(response[i]);
                i++;

                if(i === response.length)
                    clearInterval(interval);

            }, 800);
        });
    })

});
