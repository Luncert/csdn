import * as ReactDOM from 'react-dom';
import { r, rc } from './util/react-helper';
import { Component } from './com/Component';
import HomePage from './page/HomePage';

class App extends Component
{

    render()
    {
        return r(HomePage, null);
    }
    
}

ReactDOM.render(r(App, null), document.getElementById('root'));

// https://demos.creative-tim.com/material-dashboard-react/#/documentation/tutorial