
import * as $ from 'jquery';

interface Page {
    totalPages: number;
    totalElements: number;
    last: boolean;
    size: number;
    number: number;
    first: boolean;
    empty: boolean;
    content: any;
}

class Resource {

    urlBase = 'http://ai.uppfind.com:80/csdn2/';

    private buildUrl(name: string, params?: {}) {
        let url = this.urlBase + name.split('.').join('/');
        if (params) {
            url += '?';
            for (let name in params)
                url += name + '=' + params[name] + '&';
            url = url.replace(/&$/, '');
        }
        return url;
    }

    public get(name: string, params?: {}) {
        return $.get(this.buildUrl(name, params));
    }

    public create(name: string, data: {}, params?: {}) {
        return $.post({
            url: this.buildUrl(name, params),
            data: data
        });
    }

    public update(name: string, data: {}, params?: {}) {
        return $.ajax({
            url: this.buildUrl(name, params),
            method: 'PUT',
            data: data
        });
    }

    public delete(name: string, params?: {}) {
        return $.ajax({
            url: this.buildUrl(name, params),
            method: 'DELETE'
        });
    }

}

export default new Resource();

export {
    Page
}