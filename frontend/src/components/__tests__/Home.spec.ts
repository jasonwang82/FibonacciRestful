import { mount, flushPromises } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import Home from '../../views/Home.vue'
import axios from 'axios'

vi.mock('axios', async () => {
    const actual = await vi.importActual('axios')
    return {
        default: {
            ...actual,
            get: vi.fn(),
            post: vi.fn()
        }
    }
})

beforeEach(() => {
    vi.clearAllMocks()
})

describe('Home.vue', () => {
    it('renders calculator form', () => {
        const wrapper = mount(Home)
        expect(wrapper.find('input[type="number"]').exists()).toBe(true)
        expect(wrapper.find('button').text()).toBe('Calculate')
    })

    it('handles single calculation', async () => {
        const mockResult = '55'
        const mockGet = vi.spyOn(axios, 'get').mockResolvedValue({ data: mockResult })
        
        const wrapper = mount(Home)
        await wrapper.find('.single-calc input[type="number"]').setValue('10')
        await wrapper.find('.single-calc button').trigger('click')
        await flushPromises()
        
        expect(mockGet).toHaveBeenCalledWith('/api/rest/fibonacci/10')
        expect(wrapper.find('.result').text()).toContain('Result: ' + mockResult)
        
        mockGet.mockRestore()
    })

    it('handles batch calculation', async () => {
        const mockResults = { results: { '5': '5', '10': '55' } }
        const mockPost = vi.spyOn(axios, 'post').mockResolvedValue({ data: mockResults })
        
        const wrapper = mount(Home)
        await wrapper.find('.batch-calc input').setValue('5,10')
        await wrapper.find('.batch-calc button').trigger('click')
        await flushPromises()
        
        expect(mockPost).toHaveBeenCalledWith('/api/rest/fibonacci/batch', { 
            numbers: [5, 10] 
        })
        expect(wrapper.find('.result').text()).toContain('Batch Results:')
        
        mockPost.mockRestore()
    })

    it('displays error message on calculation failure', async () => {
        const mockGet = vi.spyOn(axios, 'get').mockRejectedValue(new Error('Test error'))
        
        const wrapper = mount(Home)
        await wrapper.find('.single-calc input[type="number"]').setValue('10')
        await wrapper.find('.single-calc button').trigger('click')
        await flushPromises()
        
        const resultElement = wrapper.find('.result')
        expect(resultElement.exists()).toBe(true)
        expect(resultElement.text()).toBe('Error occurred during calculation')
        
        mockGet.mockRestore()
    })
})
