
import { Props, Component } from './Component';
import { r, rc } from '../util/react-helper';

const styles = <any> require('./Loader.css');

interface CusProps extends Props {
    size: number;
}

/**
 * * props:
 *  * size: 默认10
 */
export default class Loader extends Component {

    props: CusProps;

    render() {
        const { size=10 } = this.props;
        let boxSize = size * 2, thingSize = size;
        return r('div', { className: styles.box, style: {width: boxSize, height: boxSize} },
            rc('div', 0, { className: styles.thing, style: {width: thingSize, height: thingSize} }),
            rc('div', 1, { className: styles.thing, style: {width: thingSize, height: thingSize} }),
            rc('div', 2, { className: styles.thing, style: {width: thingSize, height: thingSize} }),
            rc('div', 3, { className: styles.thing, style: {width: thingSize, height: thingSize} }),
        );
    }
    
}