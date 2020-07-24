<html>
<head>
    <link rel="stylesheet" href="css/lc_switch.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">

    <script src="https://code.jquery.com/jquery-3.3.1.min.js" type="text/javascript"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    <script src="js/lc_switch.min.js"></script>
</head>
<body>

<form class="form" data-guild-id="${guildID}" id="reactions_form">
    <h3>Общие настройки</h3>
    <#list settings as item>
        <div class="form-check row">
            <input class="form-check-input reaction_checkbox" type="checkbox" name="${item.name}" id="${item.name}-id" <#if item.active>checked</#if>
            <label class="form-check-label" for="${item.name}-id">${item.name}</label>
        </div>
    </#list>
</form>
<br/>
<form class="form" data-guild-id="${guildID}" id="replies_form">
     <h3>Доступные варианты ответов, на упоминание в чате</h3>
    <#list replies as item>
            <div class="form-check row">
                <input class="form-check-input replies_checkbox" type="checkbox" name="${item.name}" id="${item.name}-id" <#if item.active>checked</#if>
                <label class="form-check-label" for="${item.name}-id">${item.name}</label>
            </div>
        </#list>
</form>


<script type="text/javascript">
    $(document).ready(function (e) {
        $('input').lc_switch();

        var $reactionsForm = $("#reactions_form");


        $(".reaction_checkbox").on('lcs-statuschange', function () {
            var name = $(this).attr('name');
            var value = $(this).is(':checked');

            $.post('/admin/reaction', {alias:name, value:value, guildID: $reactionsForm.attr('data-guild-id')}, function(response) {
                console.log(response);
            });
        });

        $(".replies_checkbox").on('lcs-statuschange', function () {
                    var name = $(this).attr('name');
                    var value = $(this).is(':checked');

                    $.post('/admin/replies', {alias:name, value:value, guildID: $reactionsForm.attr('data-guild-id')}, function(response) {
                        console.log(response);
                    });
                });
    });

</script>
</body>
</html>

