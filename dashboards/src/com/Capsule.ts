
import { Props, Component } from './Component';
import { r, rc } from '../util/react-helper';

const styles = <any> require('./Capsule.css');

interface CusProps extends Props {
    color: string;
    name: string;
    value: any;
}

/**
 * * props:
 *  * color
 *  * name
 *  * value
 */
export default class Capsule extends Component {

    props: CusProps;

    render() {
        const { color='gray', name, value, style={} } = this.props;

        return r('div', {
                className: styles.root,
                style: style
            },
            rc('a', 'name', { className: styles.name, style: { backgroundColor: color } }, name),
            rc('a', 'value', { className: styles.value, style: { color: color, borderColor: color } }, value)
        );
    }
    
}