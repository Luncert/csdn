
import { Props, Component } from './Component';
import { r, rc } from '../util/react-helper';

const styles = <any> require('./TopPanel.css');

interface CusProps extends Props {
    onExit: () => void;
}

/**
 * * props:
 *  * onExit:
 */
export default class TopPanel extends Component {

    props: CusProps;

    render() {
        const { onExit, style={}, children } = this.props;
        return r('div', { className: styles.root, onClick: () => onExit && onExit() },
            rc('div', 0, {
                className: styles.container, style: style,
                onClick: (e: any) => { e.stopPropagation(); return false; }
            }, children)
        );
    }
    
}