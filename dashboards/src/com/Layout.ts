
import { Props, Component } from './Component';
import { r, rc } from '../util/react-helper';

const styles = <any> require('./Layout.css');

class Row extends Component {

    render() {
        const { style, children } = this.props;
        return r('div', { className: styles.row, style: style }, children);
    }
    
}

export {
    Row
}