import * as ReactDOM from 'react-dom';
import { r, rc } from './util/react-helper';
import { Component } from './com/Component';
import HomePage from './page/HomePage';

/**
 * TODO: 新数据来临的通知，filter搜索结果窗口
 */
class App extends Component
{

    render()
    {
        return r(HomePage, null);
    }
    
}

ReactDOM.render(r(App, null), document.getElementById('root'));

// https://demos.creative-tim.com/material-dashboard-react/#/documentation/tutorial