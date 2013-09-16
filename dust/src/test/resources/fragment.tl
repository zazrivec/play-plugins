<div class="row margin-bottom-10">
    <div class="col-md-12 activity">
        {{#propagation}}
            <div class="panel panel-warning">
                <div class="panel-heading">Propagácia</div>
            </div>
        {{/propagation}}
        {{^propagation}}
            {{#invitation}}
                <div class="panel panel-danger">
                    <div class="panel-heading">Pozvánka</div>
                </div>
            {{/invitation}}
            {{^invitation}}
                <div class="panel panel-info">
                    <div class="panel-heading">Realizovaná aktivita</div>
                </div>
            {{/invitation}}
        {{/propagation}}
        <div class="row">
            <div class="col-md-8 activity">
                <div class="row small-gallery">{{#files}}<a class="fancybox" data-fancybox-group="activity-{{activityId}}" href="{{img}}" title="Fotky z aktivity {{shortDescription}}"><img data-src="{{imgThumbnail}}" class="img-rounded" alt="Fotky z aktivity {{altDescription}}"></a>{{/files}}</div>
            </div>
            <div class="col-md-4">
                {{#tags.Program}}
                    <div>Program: <a href="{{programUrl}}">{{tags.Program}}</a></div>
                    <div>Organizácia: {{tags.Organization}}</div>
                    <div>Projekt: <a href="{{projectUrl}}">{{tags.Project}}</a></div>
                    <div>Mesto: {{tags.City}}</div>
                {{/tags.Program}}
            </div>
        </div>

        <div class="row">
            <div class="col-md-8">
                <span class="expandable">{{description}}</span>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <p>
                    <span class="glyphicon glyphicon-calendar"></span>
                    {{fromYMD}}
                    {{#toYMD}}
                        - {{toYMD}}
                    {{/toYMD}}
                    {{#canEdit}}
                        <span class="glyphicon glyphicon-edit"></span><a href="{{editUrl}}">&nbsp;Upraviť &raquo;</a>
                    {:else}
                        {{#loggedIn}}
                            {{{editElement}}}
                        {{/loggedIn}}
                    {{/canEdit}}
                    <span>{{shareText}}&nbsp;</span>
                    <a class="share btn btn-xs btn-facebook" data-id="{{id}}" data-href="https://www.facebook.com/sharer/sharer.php?u=" target="_blank"><span class="icon-facebook"></span> | facebook</a>&nbsp;
                    <a class="share btn btn-xs btn-twitter" data-id="{{id}}" data-href="https://twitter.com/share?url=" target="_blank"><span class="icon-twitter"></span> | twitter</a>&nbsp;
                    <a class="share btn btn-xs btn-google-plus" data-id="{{id}}" data-href="https://plus.google.com/share?url=" target="_blank"><span class="icon-google-plus"></span> | google</a>&nbsp;
                </p>
            </div>
        </div>
    </div>
</div>
