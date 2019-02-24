import * as ReactDOM from 'react-dom';
import { r } from './util/react-helper';
import { Component } from './Component';

class App extends Component
{

    render()
    {
        return r('div', null);
    }
    
}

ReactDOM.render(r(App, null), document.getElementById('root'));