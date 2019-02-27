
import { Props, Component } from './Component';
import { r, rc } from '../util/react-helper';

const styles = <any> require('./TopPanel.css');

interface CusProps extends Props {
}

/**
 * * props:
 *  * size: 默认10
 */
export default class TopPanel extends Component {

    props: CusProps;

    render() {
        const { style={}, children } = this.props;
        return r('div', { className: styles.root },
            rc('div', 0, { className: styles.container, style: style }, children)
        );
    }
    
}